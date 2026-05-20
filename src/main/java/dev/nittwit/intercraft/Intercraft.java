package dev.nittwit.intercraft;

import dev.nittwit.intercraft.block.ModBlocks;
import dev.nittwit.intercraft.block.entity.ModBlockEntities;
import dev.nittwit.intercraft.item.ModCreativeModeTabs;
import dev.nittwit.intercraft.item.ModItems;

//import org.slf4j.Logger;
//
//import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(Intercraft.MOD_ID)
public class Intercraft {
    public static final String MOD_ID = "intercraft";
//    public static final Logger LOGGER = LogUtils.getLogger();

    public Intercraft(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        NeoForge.EVENT_BUS.register(new ServerEvents());

        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Literally I don't know what to put here ;-;
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Literally I don't know what to put here ;-;
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Literally I don't know what to put here ;-;
    }
}