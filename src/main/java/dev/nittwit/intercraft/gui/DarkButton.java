package dev.nittwit.intercraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DarkButton extends Button {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "intercraft", "textures/gui/dark_button.png"
    );
    private static final ResourceLocation TEXTURE_HIGHTLIGHTED = ResourceLocation.fromNamespaceAndPath(
            "intercraft", "textures/gui/dark_button_highlighted.png"
    );
    private final int texWidth, texHeight;
    private static final int BORDER = 3;

    public DarkButton(int x, int y, int width, int height, Component text, OnPress onPress) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION);
        this.texWidth = width;
        this.texHeight = height;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        ResourceLocation tex = this.isHovered() ? TEXTURE_HIGHTLIGHTED : TEXTURE;

        int x = getX();
        int y = getY();
        int w = width;
        int h = height;

        int texW = w;
        int texH = h;

        // corners (no scaling)
        guiGraphics.blit(tex, x, y, 0, 0, BORDER, BORDER, texW, texH);
        guiGraphics.blit(tex, x + w - BORDER, y, texW - BORDER, 0, BORDER, BORDER, texW, texH);
        guiGraphics.blit(tex, x, y + h - BORDER, 0, texH - BORDER, BORDER, BORDER, texW, texH);
        guiGraphics.blit(tex, x + w - BORDER, y + h - BORDER, texW - BORDER, texH - BORDER, BORDER, BORDER, texW, texH);

        // edges (1-axis stretch)
        guiGraphics.blit(tex, x + BORDER, y, BORDER, 0, w - BORDER * 2, BORDER, texW, texH);
        guiGraphics.blit(tex, x + BORDER, y + h - BORDER, BORDER, texH - BORDER, w - BORDER * 2, BORDER, texW, texH);

        guiGraphics.blit(tex, x, y + BORDER, 0, BORDER, BORDER, h - BORDER * 2, texW, texH);
        guiGraphics.blit(tex, x + w - BORDER, y + BORDER, texW - BORDER, BORDER, BORDER, h - BORDER * 2, texW, texH);

        // center (2-axis stretch)
        guiGraphics.blit(tex,
                x + BORDER, y + BORDER,
                BORDER, BORDER,
                w - BORDER * 2, h - BORDER * 2,
                texW, texH
        );

        // text
        guiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                getMessage(),
                x + w / 2,
                y + (h - 8) / 2,
                0xFFFFFF
        );
    }}