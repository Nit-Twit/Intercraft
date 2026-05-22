package dev.nittwit.intercraft.screen;

import com.mojang.logging.LogUtils;
import dev.nittwit.intercraft.Intercraft;
import dev.nittwit.intercraft.gui.ControlButton;
import dev.nittwit.intercraft.gui.DarkButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

public class MainScreen extends AbstractContainerScreen<LaptopMainMenu> {

    public static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(Intercraft.MOD_ID, "textures/gui/villager_selection.png");
    private static final ResourceLocation SCROLLER =
            ResourceLocation.fromNamespaceAndPath(Intercraft.MOD_ID, "textures/gui/scroller_dark.png");
    private static final ResourceLocation SCROLLER_DISABLED =
            ResourceLocation.fromNamespaceAndPath(Intercraft.MOD_ID, "textures/gui/scroller_dark_disabled.png");

    private static final ResourceLocation TEXTURE_UNLINK =
            ResourceLocation.fromNamespaceAndPath(Intercraft.MOD_ID, "textures/gui/unlink.png");
    private static final ResourceLocation TEXTURE_TRADE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/item/emerald.png");


    private final int IMAGE_WIDTH = 176;
    private final int IMAGE_HEIGHT = 166;
    private final int MAX_VISIBLE = 3;

    private int SCROLLER_Y = 20;
    private int leftPos, topPos;
    private int scrollOffset = 0;
    private final List<String> villagers;

    private int selected = -1;

    private void refreshButtons() {
        this.clearWidgets();
        int count = villagers.size();

        if (count == 0) return;

        int visible = Math.min(count, MAX_VISIBLE);

        for (int i = 0; i < visible; i++) {
            int dataIndex = scrollOffset + i;
            if (dataIndex >= count) break;

            // TODO: Pass UUID to server
//            String UUID = villagers.get(dataIndex);

            int x = leftPos + 69;
            int BUTTON_HEIGHT = 16;
            int y = topPos + 20 + i * BUTTON_HEIGHT;

            if (count > MAX_VISIBLE) {
                int maxOffset = count - MAX_VISIBLE;
                int scrollTrackHeight = 48 - 15; // total pixels the scroller can travel
                SCROLLER_Y = 20 + (int) ((scrollOffset / (float) maxOffset) * scrollTrackHeight);
            } else {
                SCROLLER_Y = 20;
            }

            int BUTTON_WIDTH = 84;
            addRenderableWidget(DarkButton.darkBuilder(

                            // TODO: Set button text to villager name
                            Component.literal(String.valueOf(dataIndex)),
                            btn -> {
                                if (btn instanceof DarkButton darkBtn) {
                                    onVillagerClick(dataIndex, darkBtn);
                                }
                            })
                    .pos(x, y)
                    .selected(this.selected == dataIndex)
                    .size(BUTTON_WIDTH, BUTTON_HEIGHT)
                    .build());
        }

        /* Render Control Buttons */

        ControlButton tradeButton = ControlButton.darkBuilder(
                        Component.literal(""),
                        btn -> {
                            // TODO: Button functionality
                        })
                .pos(leftPos + 19, topPos + 27)
                .disabled(this.selected == -1)
                .size(34, 16)
                .build();
        addRenderableWidget(tradeButton);

        ControlButton unlinkButton = ControlButton.darkBuilder(
                        Component.literal(""),
                        btn -> {
                            // TODO: Button functionality
                        })
                .pos(leftPos + 19, topPos + 45)
                .disabled(this.selected == -1)
                .size(34, 16)
                .build();
        addRenderableWidget(unlinkButton);

        /* Render Images Over Controls */

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
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        int count = villagers.size();
        ResourceLocation scroller = count > MAX_VISIBLE ? SCROLLER : SCROLLER_DISABLED;
        int SCROLLER_X = 154;
        guiGraphics.blit(scroller,
                leftPos + SCROLLER_X,
                topPos + SCROLLER_Y,
                0, 0, 6, 15, 6, 15);


        guiGraphics.blit(TEXTURE_TRADE,
                leftPos + 28,
                topPos + 27,
                0, 0, 16, 16, 16, 16);

        guiGraphics.blit(TEXTURE_UNLINK,
                leftPos + 31,
                topPos + 48,
                0, 0, 10, 10, 10, 10);
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

    private void onVillagerClick(int index, Button btn) {
        if (btn instanceof DarkButton) {
            this.selected = index;
            refreshButtons();
        }
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
        guiGraphics.drawCenteredString(this.font, this.title, this.IMAGE_WIDTH / 2, 7, 0xFFFFFF);
        guiGraphics.drawString(this.font, Component.translatable("container.inventory"), 8, 72, 0xFFFFFF, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
