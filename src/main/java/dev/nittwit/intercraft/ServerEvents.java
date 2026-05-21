package dev.nittwit.intercraft;

import com.mojang.logging.LogUtils;
import dev.nittwit.intercraft.block.ModBlocks;
import dev.nittwit.intercraft.block.entity.ModBlockEntities;
import dev.nittwit.intercraft.util.NameManager;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServerEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public void onVillagerInteract(@NotNull PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Villager villager)) return;
        Player player = event.getEntity();

        ItemStack item = player.getMainHandItem();
        if (!player.isCrouching() || !item.is(ModBlocks.BUILDERS_LAPTOP.asItem())) return;

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        CustomData data = item.get(DataComponents.BLOCK_ENTITY_DATA);
        CompoundTag tag = (data != null) ? data.copyTag() : new CompoundTag();

        ListTag list = tag.getList("villagers_linked", StringTag.TAG_STRING);
        boolean villagerLinked = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.getString(i).equals(villager.getUUID().toString())) {
                villagerLinked = true;
                break;
            }
        }

        if (event.getLevel().isClientSide) {
            if (villagerLinked) {
                event.getLevel().playSound(player, BlockPos.containing(villager.position()), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1.0F, 1.0F);
                player.displayClientMessage(
                        Component.translatable("intercraft.messages.villagerlinked.alreadylinked").setStyle(Style.EMPTY
                                .withColor(ChatFormatting.RED)
                                .withBold(true)), true
                );
            } else {
                if (villager.getVillagerData().getProfession() == VillagerProfession.NONE ||
                    villager.getVillagerData().getProfession() == VillagerProfession.NITWIT) {
                    event.getLevel().playSound(player, BlockPos.containing(villager.position()), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    player.displayClientMessage(
                            Component.translatable("intercraft.messages.villagerlinked.noprofession").setStyle(Style.EMPTY
                                    .withColor(ChatFormatting.RED)
                                    .withBold(true)), true
                    );
                } else {
                    event.getLevel().playSound(player, BlockPos.containing(villager.position()), SoundEvents.VILLAGER_CELEBRATE, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    player.displayClientMessage(
                            Component.translatable("intercraft.messages.villagerlinked.success").setStyle(Style.EMPTY
                                    .withColor(ChatFormatting.GREEN)
                                    .withBold(true)), true
                    );
                }
            }
        } else if (!villagerLinked) {
            // read existing list from BLOCK_ENTITY_DATA
            if (villager.getVillagerData().getProfession() == VillagerProfession.NONE ||
                    villager.getVillagerData().getProfession() == VillagerProfession.NITWIT) return;
            CustomData beData = item.get(DataComponents.BLOCK_ENTITY_DATA);
            CompoundTag existing = (beData != null) ? beData.copyTag() : new CompoundTag();

            ListTag listTags = existing.getList("villagers_linked", StringTag.TAG_STRING);

            String id = villager.getUUID().toString();
            listTags.add(StringTag.valueOf(id));
            existing.put("villagers_linked", listTags);

            // write back (adds id automatically)
            BlockItem.setBlockEntityData(item, ModBlockEntities.BUILDERS_LAPTOP_BE.get(), existing);

            // update glint
            if (!listTags.isEmpty()) {
                item.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            } else {
                item.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
            }
        }
    }

    @SubscribeEvent
    public void onFinalizeSpawn(@NotNull FinalizeSpawnEvent event) {
        if (event.getEntity() instanceof Villager villager) {
            String name = Intercraft.NAME_MANAGER.getRandomName();
            villager.setCustomName(Component.literal(name));
        }
    }
}