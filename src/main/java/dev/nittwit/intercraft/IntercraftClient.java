package dev.nittwit.intercraft;

import com.mojang.logging.LogUtils;
import dev.nittwit.intercraft.screen.LaptopMainMenu;
import dev.nittwit.intercraft.screen.MainScreen;
import dev.nittwit.intercraft.screen.ModMenus;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Intercraft.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Intercraft.MOD_ID, value = Dist.CLIENT)
public class IntercraftClient {
    public static final Logger LOGGER = LogUtils.getLogger();

    public IntercraftClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.<LaptopMainMenu, MainScreen>register(
                (MenuType<LaptopMainMenu>) ModMenus.LAPTOP_MAIN_MENU.get(),
                (menu, inv, title) -> new MainScreen(menu, inv, title)
        );
}}
