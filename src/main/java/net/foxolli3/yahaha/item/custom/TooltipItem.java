package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.block.ModBlocks;
import net.foxolli3.yahaha.item.Moditems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TooltipItem extends Item {
    String tooltip;

    public TooltipItem(Properties pProperties, String tooltip) {
        super(pProperties);
        this.tooltip = tooltip;
    }


    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
            pTooltipComponents.add(Component.literal(tooltip));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}