package com.partlymanu.refinedutils.network;

import com.partlymanu.refinedutils.RefinedUtils;
import com.partlymanu.refinedutils.block.ExposedInterfaceBlockEntity;
import com.refinedmods.refinedstorage.api.storage.AccessMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AccessModePacket(BlockPos pos, AccessMode accessMode) implements CustomPacketPayload {

    public static final Type<AccessModePacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(RefinedUtils.MODID, "access_mode")
    );

    public static final StreamCodec<FriendlyByteBuf, AccessModePacket> CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeBlockPos(packet.pos());
                buf.writeEnum(packet.accessMode());
            },
            buf -> new AccessModePacket(buf.readBlockPos(), buf.readEnum(AccessMode.class))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AccessModePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (player.level().getBlockEntity(packet.pos()) instanceof ExposedInterfaceBlockEntity be) {
                    be.setAccessMode(packet.accessMode());
                }
            }
        });
    }
}