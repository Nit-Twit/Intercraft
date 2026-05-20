package dev.nittwit.intercraft.util;

import dev.nittwit.intercraft.Intercraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> LINKED_VILLAGERS = createTag("linked_villagers");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Intercraft.MOD_ID, name));
        }
    }
}

