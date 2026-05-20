package dev.nittwit.intercraft.item;

import dev.nittwit.intercraft.Intercraft;
import dev.nittwit.intercraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Intercraft.MOD_ID);

    public static final Supplier<CreativeModeTab> INTERCRAFT_TAB = CREATIVE_MODE_TAB.register("mod_creative_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.BUILDERS_LAPTOP.get()))
                    .title(Component.translatable("creativetab.intercraft.main"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.BUILDERS_LAPTOP);
                        output.accept(ModItems.LOGIC_COMPONENT);
                    }).build());

    public static void register (IEventBus bus) {
        CREATIVE_MODE_TAB.register(bus);
    }
}
