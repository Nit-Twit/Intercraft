package dev.nittwit.intercraft.screen;

import com.mojang.logging.LogUtils;
import dev.nittwit.intercraft.gui.DarkButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AbstractContainerScreen<LaptopMainMenu> {

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(
            "intercraft", "textures/gui/villager_selection.png"
    );
    private static final ResourceLocation DARK_BUTTON = ResourceLocation.fromNamespaceAndPath(
            "intercraft", "textures/gui/dark_button.png");
    private static final ResourceLocation SCROLLER = ResourceLocation.fromNamespaceAndPath(
            "intercraft", "textures/gui/scroller_dark.png");
    private static final ResourceLocation SCROLLER_DISABLED = ResourceLocation.fromNamespaceAndPath(
            "intercraft", "textures/gui/scroller_dark_disabled.png");


    private final int
            IMAGE_WIDTH = 176,
            IMAGE_HEIGHT = 166,
            BUTTON_X = 42,
            BUTTON_Y = 19,
            BUTTON_WIDTH = 84,
            BUTTON_HEIGHT = 10,
            MAX_VISIBLE = 5,
            SCROLLER_X = 127,
            SCROLLER_Y = 19;

    private int leftPos, topPos;
    private int scrollOffset = 0;
    private List<String> villagers = new ArrayList<>(); // populate this from your menu/BE

    private void refreshButtons() {
        this.clearWidgets();
        int count = villagers.size();

        if (count == 0) return;

        int visible = Math.min(count, MAX_VISIBLE);

        for (int i = 0; i < visible; i++) {
            int dataIndex = scrollOffset + i;
            if (dataIndex >= count) break;
            String name = villagers.get(dataIndex);
            int x = leftPos + BUTTON_X;
            int y = topPos + BUTTON_Y + i * BUTTON_HEIGHT;

            ClientLevel level = Minecraft.getInstance().level;

            addRenderableWidget(Button.builder(Component.literal(name), btn -> onVillagerClick(dataIndex)).pos(x, y).size(BUTTON_WIDTH, BUTTON_HEIGHT).build());
        }
    }


    public MainScreen(LaptopMainMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        villagers = menu.getLinkedVillagers();
        LOGGER.info(villagers.toString());
    }

    @Override
    protected void init() {

        // TODO: Add buttons

        leftPos = (this.width - IMAGE_WIDTH) / 2;
        topPos = (this.height - IMAGE_HEIGHT) / 2;
        this.refreshButtons();

        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        int count = villagers.size();
        if (count >= 5) {
            // show scroller (disabled if exactly 5, active if more)
            ResourceLocation scroller = count > MAX_VISIBLE ? SCROLLER : SCROLLER_DISABLED;
            guiGraphics.blit(scroller, leftPos + SCROLLER_X, topPos + SCROLLER_Y, 0, 0, 6, 15, 6, 15);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int count = villagers.size();
        if (count <= MAX_VISIBLE) return false;
        int maxOffset = count - MAX_VISIBLE;
        scrollOffset = (int) Math.clamp(scrollOffset - scrollY, 0, maxOffset);
        refreshButtons();
        return true;
    }

    private void onVillagerClick(int index) {
        // handle villager selection
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(BACKGROUND, leftPos, topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    @Override
    protected void renderBlurredBackground(float partialTick) {
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawCenteredString(this.font, this.title, this.IMAGE_WIDTH / 2, 6, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("container.inventory"), 8, 72, 0xFFFFFF, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
