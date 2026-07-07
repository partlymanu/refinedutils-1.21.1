package com.partlymanu.refinedutils.menu;

import com.partlymanu.refinedutils.block.ModBlockEntities;
import com.partlymanu.refinedutils.block.ExposedInterfaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import com.refinedmods.refinedstorage.api.storage.AccessMode;
import java.util.ArrayList;
import java.util.List;

public class ExposedInterfaceMenu extends AbstractContainerMenu {

    private final List<OperationLogEntry> log;
    private final AccessMode accessMode;
    private final BlockPos blockPos;

    // Called on the server when the player opens the screen
    public ExposedInterfaceMenu(int containerId, Inventory playerInventory, ExposedInterfaceBlockEntity blockEntity) {
        super(ModMenuTypes.EXPOSED_INTERFACE.get(), containerId);
        this.log = new ArrayList<>(blockEntity.getLog());
        this.accessMode = blockEntity.getAccessMode();
        this.blockPos = blockEntity.getBlockPos();
    }

    // Called on the client when the screen packet arrives
    public ExposedInterfaceMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        super(ModMenuTypes.EXPOSED_INTERFACE.get(), containerId);
        int size = buf.readInt();
        this.log = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            log.add(OperationLogEntry.fromBytes(buf));
        }
        this.accessMode = buf.readEnum(AccessMode.class);
        this.blockPos = buf.readBlockPos();
    }

    public List<OperationLogEntry> getLog() {
        return log;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}