package net.foxolli3.yahaha.block.custom;

import com.mojang.serialization.MapCodec;
import net.foxolli3.yahaha.block.entity.ModBlockEntities;
import net.foxolli3.yahaha.block.entity.WandConstructionTableBlockEntity;
import net.foxolli3.yahaha.screen.FredrickWandSchematicScreen;
import net.foxolli3.yahaha.screen.ModMenuTypes;
import net.foxolli3.yahaha.screen.WandConstructionTableMenu;
import net.foxolli3.yahaha.screen.WandConstructionTableScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WandConstructionTable extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 12, 15);

    public WandConstructionTable(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContest) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof WandConstructionTableBlockEntity) {
                ((WandConstructionTableBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    /*@Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof WandConstructionTableBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (WandConstructionTableBlockEntity)entity, pPos);
            } else {
                throw new IllegalStateException("Missing Container Provider, Not Very Kingdom");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }*/

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(state.getMenuProvider(level, pos));
            if (!level.isClientSide()) {
                BlockEntity entity = level.getBlockEntity(pos);
                ServerPlayer theplayer = (ServerPlayer) player;
                if (entity instanceof WandConstructionTableBlockEntity) {
                    //ServerPlayer.openScreen(((ServerPlayer)pPlayer), (Breeder_Tile)entity, pPos);
                    theplayer.openMenu((WandConstructionTableBlockEntity) entity, pos);
                } else {
                    throw new IllegalStateException("Container provider is missing");
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            ServerPlayer theplayer = (ServerPlayer) player;
            if(entity instanceof WandConstructionTableBlockEntity) {
                theplayer.openMenu((WandConstructionTableBlockEntity)entity, pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return ItemInteractionResult.sidedSuccess(true);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WandConstructionTableBlockEntity(pPos, pState);
    }



    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }
        return createTickerHelper(pBlockEntityType, ModBlockEntities.WAND_TABLE_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }
}
