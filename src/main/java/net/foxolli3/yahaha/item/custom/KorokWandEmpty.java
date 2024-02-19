package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.block.ModBlocks;
import net.foxolli3.yahaha.item.Moditems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class KorokWandEmpty extends Item {

    public KorokWandEmpty(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            BlockPos positionClicked = pContext.getClickedPos();
            Level level = pContext.getLevel();
            Player player = pContext.getPlayer();
            BlockState state = pContext.getLevel().getBlockState(positionClicked);
            ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
            Item item = itemstack.getItem();
            boolean isKorokBlock = pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() == ModBlocks.KOROK_SEED_BLOCK.get();

            if (isKorokBlock == true) {
                if (item == Moditems.KOROK_WAND_EMPTY.get()) {
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Moditems.KOROK_WAND_BLOCK.get(),1));
                    level.destroyBlock(positionClicked, false);

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.yahaha.korok_wand_empty"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}