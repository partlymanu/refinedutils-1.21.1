package com.partlymanu.refinedutils.menu;

import com.partlymanu.refinedutils.network.AccessModePacket;
import com.refinedmods.refinedstorage.api.storage.AccessMode;
import com.refinedmods.refinedstorage.common.support.widget.AbstractSideButtonWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class AccessModeSideButton extends AbstractSideButtonWidget {

    private static final ResourceLocation INSERT =
            ResourceLocation.fromNamespaceAndPath("refinedstorage", "widget/side_button/storage/access_mode/insert");
    private static final ResourceLocation EXTRACT =
            ResourceLocation.fromNamespaceAndPath("refinedstorage", "widget/side_button/storage/access_mode/extract");
    private static final ResourceLocation INSERT_EXTRACT =
            ResourceLocation.fromNamespaceAndPath("refinedstorage", "widget/side_button/storage/access_mode/insert_extract");

    private final BlockPos blockPos;
    private AccessMode accessMode;

    public AccessModeSideButton(BlockPos blockPos, AccessMode initialMode) {
        super(btn -> {
            // handled via overriding onPress below
        });
        this.blockPos = blockPos;
        this.accessMode = initialMode;
    }

    @Override
    public void onPress() {
        accessMode = switch (accessMode) {
            case INSERT_EXTRACT -> AccessMode.INSERT;
            case INSERT -> AccessMode.EXTRACT;
            case EXTRACT -> AccessMode.INSERT_EXTRACT;
        };
        PacketDistributor.sendToServer(new AccessModePacket(blockPos, accessMode));
    }

    @Override
    protected ResourceLocation getSprite() {
        return switch (accessMode) {
            case INSERT_EXTRACT -> INSERT_EXTRACT;
            case INSERT -> INSERT;
            case EXTRACT -> EXTRACT;
        };
    }

    @Override
    protected MutableComponent getTitle() {
        return Component.translatable("gui.refinedstorage.access_mode");
    }

    @Override
    protected List<MutableComponent> getSubText() {
        return switch (accessMode) {
            case INSERT_EXTRACT -> List.of(Component.translatable("gui.refinedstorage.access_mode.insert_extract")
                    .withStyle(ChatFormatting.GRAY));
            case INSERT -> List.of(Component.translatable("gui.refinedstorage.access_mode.insert")
                    .withStyle(ChatFormatting.GRAY));
            case EXTRACT -> List.of(Component.translatable("gui.refinedstorage.access_mode.extract")
                    .withStyle(ChatFormatting.GRAY));
        };
    }
}