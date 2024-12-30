package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.block.ModBlocks;
import net.foxolli3.yahaha.component.ModDataComponentTypes;
import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.entity.custom.FredrickMob;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.screen.FredrickStatScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlueChuchuJellyItem extends Item {

    public BlueChuchuJellyItem(Properties pProperties) {
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
                if (item == Moditems.BLUE_CHUCHU_JELLY.get()) {
                    itemstack.shrink(1);
                    level.destroyBlock(positionClicked, false);
                    level.setBlock(positionClicked, ModBlocks.WATERY_KOROK_BLOCK.get().defaultBlockState(),1);

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.blue_chuchu_jelly_shift"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.chuchu_jelly"));
        }
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}