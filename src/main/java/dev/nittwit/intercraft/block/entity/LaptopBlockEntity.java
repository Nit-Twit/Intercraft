package dev.nittwit.intercraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LaptopBlockEntity extends BlockEntity {
    private static final String LINKED_KEY = "villagers_linked";
    private final List<String> LINKED_VILLAGERS = new ArrayList<>();

    public LaptopBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.BUILDERS_LAPTOP_BE.get(), pos, blockState);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        LINKED_VILLAGERS.clear();
        ListTag list = tag.getList(LINKED_KEY, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            LINKED_VILLAGERS.add(list.getString(i));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ListTag list = new ListTag();
        for (String s : LINKED_VILLAGERS) {
            list.add(StringTag.valueOf(s));
        }
        tag.put(LINKED_KEY, list);
    }

    public void addTag(String tag) {
        LINKED_VILLAGERS.add(tag);
        setChanged();
    }


    public List<String> getLinkedVillagers() {
        return List.copyOf(LINKED_VILLAGERS);
    }
}
