package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.block.ModBlocks;
import net.foxolli3.yahaha.item.Moditems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class WhiteChuchuJellyItem extends Item {

    public WhiteChuchuJellyItem(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            BlockPos positionClicked = pContext.getClickedPos();
            Level level = pContext.getLevel();
            Player player = pContext.getPlayer();
            BlockState state = pContext.getLevel().getBlockState(positionClicked);
            ItemStack itemstack = player.getItemInHand(pContext.getHand());
            Item item = itemstack.getItem();
            boolean isKorokBlock = pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() == ModBlocks.KOROK_SEED_BLOCK.get();

            if (isKorokBlock == true) {
                if (item == Moditems.WHITE_CHUCHU_JELLY.get()) {
                    itemstack.shrink(1);
                    level.destroyBlock(positionClicked, false);
                    level.setBlock(positionClicked, ModBlocks.ICY_KOROK_BLOCK.get().defaultBlockState(),1);

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }
}