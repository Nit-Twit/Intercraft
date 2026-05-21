package dev.nittwit.intercraft.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import java.util.List;

public class LaptopMenuProvider implements MenuProvider {
    private final List<String> dataList;

    // Pass the list of strings directly into the constructor
    public LaptopMenuProvider(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Component getDisplayName() {
        // Sets the title displayed at the top of the GUI screen
        return Component.translatable("intercraft.gui.laptop");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
        // Passes the data list to the common menu constructor (usually for the server)
        return new LaptopMainMenu(windowId, inventory, this.dataList);
    }
}
