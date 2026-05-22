package dev.nittwit.intercraft.gui;

import dev.nittwit.intercraft.Intercraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ControlButton extends Button {

    // Replace "modid" with your actual mod ID
    private static final ResourceLocation TEXTURE_NORMAL =
            ResourceLocation.fromNamespaceAndPath(Intercraft.MOD_ID, "textures/gui/control_button.png");
    private static final ResourceLocation TEXTURE_HIGHLIGHTED =
            ResourceLocation.fromNamespaceAndPath(Intercraft.MOD_ID, "textures/gui/control_button_highlighted.png");
    private static final ResourceLocation TEXTURE_DISABLED =
            ResourceLocation.fromNamespaceAndPath(Intercraft.MOD_ID, "textures/gui/control_button_disabled.png");

    private boolean disabled = false;

    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        private int x, y, width = 200, height = 20;
        private boolean disabled = false;  // add this

        public Builder(Component message, OnPress onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public Builder bounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public ControlButton build() {
            ControlButton temp = new ControlButton(x, y, width, height, message, onPress);
            if (disabled) temp.disable();
            return temp;
        }
    }

    public static Builder darkBuilder(Component message, OnPress onPress) {
        return new Builder(message, onPress);
    }

    public ControlButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    public static ControlButton create(int x, int y, int width, int height, Component message, OnPress onPress) {
        return new ControlButton(x, y, width, height, message, onPress);
    }

    public void disable() {
        this.disabled = true;
    }

    public void enable() {
        this.disabled = false;
    }

    public boolean isdisabled() {
        return disabled;
    }

    private ResourceLocation getTexture() {
        boolean highlighted = this.isHovered() || this.isFocused();
        if (disabled) {
            return TEXTURE_DISABLED;
        } else {
            return highlighted ? TEXTURE_HIGHLIGHTED : TEXTURE_NORMAL;
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation texture = getTexture();

        // Draw the button texture stretched to fill the widget bounds
        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);

        // Draw the button label centered
        guiGraphics.drawCenteredString(
                net.minecraft.client.Minecraft.getInstance().font,
                this.getMessage(),
                this.getX() + this.width / 2,
                this.getY() + (this.height - 8) / 2,
                0xFFFFFF
        );
    }
}