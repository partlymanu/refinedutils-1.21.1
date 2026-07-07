package com.partlymanu.refinedutils.block;

import com.partlymanu.refinedutils.menu.ExposedInterfaceMenu;
import com.refinedmods.refinedstorage.common.support.network.AbstractBaseNetworkNodeContainerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.state.BlockState;
import com.partlymanu.refinedutils.menu.OperationLogEntry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import com.refinedmods.refinedstorage.api.storage.AccessMode;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

public class ExposedInterfaceBlockEntity
        extends AbstractBaseNetworkNodeContainerBlockEntity<ExposedInterfaceNetworkNode> implements MenuProvider {

    private final List<OperationLogEntry> log = new ArrayList<>();

    private AccessMode accessMode = AccessMode.INSERT_EXTRACT;

    public ExposedInterfaceBlockEntity(BlockPos pos, BlockState state) {
        super(
                ModBlockEntities.EXPOSED_INTERFACE_BE.get(),
                pos,
                state,
                new ExposedInterfaceNetworkNode(2L)
        );
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
        setChanged();
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("accessMode", accessMode.ordinal());
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("accessMode")) {
            accessMode = AccessMode.values()[tag.getInt("accessMode")];
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ExposedInterfaceMenu(containerId, inventory, this);
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable("block.refinedutils.exposed_interface");
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buf) {
        buf.writeInt(log.size());
        for (OperationLogEntry entry : log) {
            entry.toBytes(buf);
        }
        buf.writeEnum(accessMode);
        buf.writeBlockPos(worldPosition);
    }

    public void addLogEntry(OperationLogEntry entry) {
        if (log.size() >= 8) {
            log.removeFirst();
        }
        log.add(entry);
    }

    public List<OperationLogEntry> getLog() {
        return log;
    }

    public ExposedInterfaceNetworkNode getMainNetworkNode() {
        return mainNetworkNode;
    }
}