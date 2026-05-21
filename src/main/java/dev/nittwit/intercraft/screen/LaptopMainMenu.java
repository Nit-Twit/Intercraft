package dev.nittwit.intercraft.screen;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LaptopMainMenu extends AbstractContainerMenu {

    List<String> LINKED_VILLAGERS = new ArrayList<>();

    public LaptopMainMenu(int windowId, Inventory playerInventory, List<String> LINKED_VILLAGERS) {
        super(ModMenus.LAPTOP_MAIN_MENU.get(), windowId);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        this.LINKED_VILLAGERS = LINKED_VILLAGERS;
    }

    public LaptopMainMenu(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
//        super(ModMenus.LAPTOP_MAIN_MENU.get(), windowId);
//        addPlayerInventory(playerInventory);
//        addPlayerHotbar(playerInventory);
//        this.LINKED_VILLAGERS = data.readList(FriendlyByteBuf::readUtf);
        this(windowId, playerInventory, (List<String>) data.readCollection(ArrayList::new, FriendlyByteBuf::readUtf));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public List<String> getLinkedVillagers() {
        return this.LINKED_VILLAGERS;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
