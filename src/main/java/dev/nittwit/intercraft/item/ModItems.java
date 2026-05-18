package dev.nittwit.intercraft.item;

import dev.nittwit.intercraft.Intercraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Intercraft.MOD_ID);

    public static final DeferredItem<Item> LOGIC_COMPONENT = ITEMS.register("logic_component",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));



    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
