package dev.nittwit.intercraft.block.custom;

import com.mojang.serialization.MapCodec;
import dev.nittwit.intercraft.block.entity.LaptopBlockEntity;
import dev.nittwit.intercraft.block.entity.ModBlockEntities;
import dev.nittwit.intercraft.screen.LaptopMainMenu;
import dev.nittwit.intercraft.screen.LaptopMenuProvider;
import dev.nittwit.intercraft.screen.MainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BuildersLaptopBLock extends BaseEntityBlock {

    public static final MapCodec<BuildersLaptopBLock> CODEC = simpleCodec(BuildersLaptopBLock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape SHAPE_NORTH = Block.box(1, 0, 0.5, 15, 1, 11.5);
    private static final VoxelShape SHAPE_SOUTH = Block.box(1, 0, 4.5, 15, 1, 15.5);
    private static final VoxelShape SHAPE_EAST = Block.box(4.5, 0, 1, 15.5, 1, 15);
    private static final VoxelShape SHAPE_WEST = Block.box(0.5, 0, 1, 11.5, 1, 15);

    public BuildersLaptopBLock(Properties properties) {
        super(properties);
    }

    /* CUSTOM BLOCK MODEL METHODS */

    @NotNull
    @Override
    protected VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction FACING = state.getValue(BuildersLaptopBLock.FACING);
        if (FACING.equals(Direction.NORTH)) {
            return SHAPE_NORTH;
        } else if (FACING.equals(Direction.SOUTH)) {
            return  SHAPE_SOUTH;
        } else if (FACING.equals(Direction.EAST)) {
            return  SHAPE_EAST;
        } else if (FACING.equals(Direction.WEST)) {
            return  SHAPE_WEST;
        } else {
            return SHAPE_NORTH;
        }
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /* BLOCK ENTITY METHODS */

    @NotNull
    @Override
    protected RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new LaptopBlockEntity(blockPos, blockState);
    }

    @NotNull
    @Override
    protected ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @NotNull
    @Override
    protected InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide && !player.isCrouching()) {
            BlockPos blockPos;
            BlockEntity laptop = level.getBlockEntity(pos);

            List<String> LINKED_VILLAGERS =
                    (laptop instanceof LaptopBlockEntity laptopBe)
                            ? laptopBe.get()
                            : new ArrayList<>();

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new LaptopMenuProvider(LINKED_VILLAGERS), buffer -> {
                    buffer.writeCollection(LINKED_VILLAGERS, FriendlyByteBuf::writeUtf);
                });
            }

//            player.openMenu(new SimpleMenuProvider(
//                    (windowId, inv, p )-> new LaptopMainMenu(windowId, inv, LINKED_VILLAGERS),
//                    Component.translatable("intercraft.gui.laptop")
//            ));
            return InteractionResult.SUCCESS;
        } else if (!level.isClientSide && player.isCrouching()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            ItemStack stack = new ItemStack(this.asItem());

            if (blockEntity != null) {
                blockEntity.saveToItem(stack, level.registryAccess());

                // after saveToItem(...)
                if (blockEntity instanceof LaptopBlockEntity be && !be.getLinkedVillagers().isEmpty()) {
                    stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
                } else {
                    stack.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
                }
            }

            level.removeBlock(pos, false);
            level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            player.getInventory().setItem(
                    player.getInventory().selected,
                    stack
            );
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (!level.isClientSide && blockEntity instanceof LaptopBlockEntity be) {
            ItemStack stack = new ItemStack(this.asItem());

            CompoundTag tag = new CompoundTag();
            be.saveAdditional(tag, level.registryAccess());
            BlockItem.setBlockEntityData(stack, ModBlockEntities.BUILDERS_LAPTOP_BE.get(), tag);

            if (!be.getLinkedVillagers().isEmpty()) {
                stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
            }

            popResource(level, pos, stack);
        } else {
            super.playerDestroy(level, player, pos, state, blockEntity, tool);
        }
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide) {
            Player player = (placer instanceof Player p) ? p : null;

            BlockItem.updateCustomBlockEntityTag(level, player, pos, stack);
        }
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest, @NotNull FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, true, fluid);
    }
}
