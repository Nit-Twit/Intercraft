package dev.nittwit.intercraft.block.entity;

import dev.nittwit.intercraft.Intercraft;
import dev.nittwit.intercraft.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Intercraft.MOD_ID);

    public static final Supplier<BlockEntityType<LaptopBlockEntity>> BUILDERS_LAPTOP_BE =
            BLOCK_ENTITIES.register("builders_laptop_be", () -> BlockEntityType.Builder.of(
                    LaptopBlockEntity::new, ModBlocks.BUILDERS_LAPTOP.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
