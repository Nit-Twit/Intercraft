package dev.nittwit.intercraft.screen;

import dev.nittwit.intercraft.Intercraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Intercraft.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<LaptopMainMenu>> LAPTOP_MAIN_MENU =
            MENUS.register("laptop_main_menu", () ->
                    IMenuTypeExtension.create((windowId, inv, data) -> new LaptopMainMenu(windowId, inv, data)));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
