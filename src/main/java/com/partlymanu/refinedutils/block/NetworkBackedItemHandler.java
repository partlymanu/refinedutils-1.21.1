package com.partlymanu.refinedutils.block;

import com.partlymanu.refinedutils.menu.OperationLogEntry;
import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.api.resource.ResourceAmount;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class NetworkBackedItemHandler implements IItemHandler {

    private final ExposedInterfaceBlockEntity blockEntity;

    public NetworkBackedItemHandler(ExposedInterfaceBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    // Builds a snapshot of the network's current resource list as a list.
    // We do this on every call rather than caching, so it's always up to date.
    private List<ResourceAmount> getNetworkResources() {
        Network network = blockEntity.getMainNetworkNode().getNetwork();
        if (network == null) {
            return List.of();
        }
        StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
        return new ArrayList<>(storage.getAll());
    }

    @Override
    public int getSlots() {
        int size = getNetworkResources().size();
//        RefinedUtils.LOGGER.info("getSlots called, returning: {}", size);
        return (size+1);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        List<ResourceAmount> resources = getNetworkResources();
        // Last slot is the dedicated insertion slot — always empty
        if (slot >= resources.size()) {
            return ItemStack.EMPTY;
        }
        if (slot < 0) {
            return ItemStack.EMPTY;
        }
        ResourceAmount ra = resources.get(slot);
        if (!(ra.resource() instanceof ItemResource itemResource)) {
            return ItemStack.EMPTY;
        }
        // Report real amount, capped at Integer.MAX_VALUE since ItemStack uses int
        return itemResource.toItemStack(Math.min(ra.amount(), Integer.MAX_VALUE));
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
//        RefinedUtils.LOGGER.info("insert item trigger");
        if (blockEntity.getAccessMode().isExtractOnly()) return stack;
        if (stack.isEmpty()) return stack;
        Network network = blockEntity.getMainNetworkNode().getNetwork();
        if (network == null) return stack;
//        RefinedUtils.LOGGER.info("Inserting: {} x{}, simulate: {}", stack.getItem(), stack.getCount(), simulate);
        StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
        ItemResource resource = ItemResource.ofItemStack(stack);
        long inserted = storage.insert(
                resource,
                stack.getCount(),
                simulate ? Action.SIMULATE : Action.EXECUTE,
                Actor.EMPTY
        );
//        RefinedUtils.LOGGER.info("Inserted: {}", inserted);

        if (!simulate && inserted > 0) {
            blockEntity.addLogEntry(new OperationLogEntry(
                    OperationLogEntry.Type.INSERT,
                    resource.toItemStack(inserted),
                    (int) inserted
            ));
        }

        if (inserted == stack.getCount()) return ItemStack.EMPTY;
        ItemStack remainder = stack.copy();
        remainder.setCount((int) (stack.getCount() - inserted));

        return remainder;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (blockEntity.getAccessMode().isInsertOnly()) return ItemStack.EMPTY;
        List<ResourceAmount> resources = getNetworkResources();
        if (slot < 0 || slot >= resources.size()) return ItemStack.EMPTY;
        ResourceAmount ra = resources.get(slot);
        if (!(ra.resource() instanceof ItemResource itemResource)) return ItemStack.EMPTY;
        Network network = blockEntity.getMainNetworkNode().getNetwork();
        if (network == null) return ItemStack.EMPTY;
        StorageNetworkComponent storage = network.getComponent(StorageNetworkComponent.class);
        long extracted = storage.extract(
                itemResource,
                amount,
                simulate ? Action.SIMULATE : Action.EXECUTE,
                Actor.EMPTY
        );
        if (extracted == 0) return ItemStack.EMPTY;

        if (!simulate && extracted > 0) {
            blockEntity.addLogEntry(new OperationLogEntry(
                    OperationLogEntry.Type.EXTRACT,
                    itemResource.toItemStack(extracted),
                    (int) extracted
            ));
        }

        return itemResource.toItemStack(extracted);
    }

    @Override
    public int getSlotLimit(int slot) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}