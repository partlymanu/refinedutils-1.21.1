package com.partlymanu.refinedutils.menu;

import com.refinedmods.refinedstorage.common.support.AbstractBaseScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExposedInterfaceScreen extends AbstractBaseScreen<ExposedInterfaceMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "refinedutils", "textures/gui/exposed_interface.png"
    );

    private static final int LOG_START_Y = 25;
    private static final int LOG_ROW_HEIGHT = 20;

    public ExposedInterfaceScreen(ExposedInterfaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 254;
        this.imageHeight = 231;
    }

    @Override
    protected void init() {
        super.init();
        addSideButton(new AccessModeSideButton(menu.getBlockPos(), menu.getAccessMode()));
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Only render the title, not the inventory label
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        super.renderBg(graphics, delta, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        var log = menu.getLog();
        for (int i = 0; i < log.size(); i++) {
            OperationLogEntry entry = log.get(i);
            int rowY = y + LOG_START_Y + (i * LOG_ROW_HEIGHT);

            // Draw item icon
            graphics.renderItem(entry.getStack(), x + 15, rowY);

            // Draw arrow and text
            String prefix = entry.getType() == OperationLogEntry.Type.INSERT ? "→ " : "← ";
            String text = prefix + entry.getAmount() + "x " + entry.getStack().getHoverName().getString();
            graphics.drawString(font, text, x + 35, rowY + 4,
                    entry.getType() == OperationLogEntry.Type.INSERT ? 0x00AA00 : 0xAA0000, false);
        }

        if (log.isEmpty()) {
            graphics.drawString(font, "No operations yet.", x + 15, y + LOG_START_Y + 4, 0x666666, false);
        }
    }
}