package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.entity.custom.BlueChuchuMob;
import net.foxolli3.yahaha.entity.custom.RedChuchuMob;
import net.foxolli3.yahaha.entity.custom.WhiteChuchuMob;
import net.foxolli3.yahaha.entity.custom.YellowChuchuMob;
import net.foxolli3.yahaha.item.Moditems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ChuchuFinder extends Item {

    public ChuchuFinder(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        var level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!player.getCooldowns().isOnCooldown(this)) {
            player.getCooldowns().addCooldown(this, 20);

            BlockPos usePos = context.getClickedPos();
            ServerLevel serverLevel = (ServerLevel) level;

            Vec3 center = Vec3.atCenterOf(usePos);
            double radius = 60.0;
            AABB area = new AABB(center.subtract(radius, radius, radius), center.add(radius, radius, radius));

            List<BlueChuchuMob> blueChuchus = serverLevel.getEntitiesOfClass(BlueChuchuMob.class, area);
            List<RedChuchuMob> redChuchus = serverLevel.getEntitiesOfClass(RedChuchuMob.class, area);
            List<YellowChuchuMob> yellowChuchus = serverLevel.getEntitiesOfClass(YellowChuchuMob.class, area);
            List<WhiteChuchuMob> whiteChuchus = serverLevel.getEntitiesOfClass(WhiteChuchuMob.class, area);

            blueChuchus.forEach(chuchu -> chuchu.setVisible(true));
            redChuchus.forEach(chuchu -> chuchu.setVisible(true));
            yellowChuchus.forEach(chuchu -> chuchu.setVisible(true));
            whiteChuchus.forEach(chuchu -> chuchu.setVisible(true));

            blueChuchus.forEach(chuchu -> shootParticleLine(serverLevel, chuchu.blockPosition()));
            redChuchus.forEach(chuchu -> shootParticleLine(serverLevel, chuchu.blockPosition()));
            yellowChuchus.forEach(chuchu -> shootParticleLine(serverLevel, chuchu.blockPosition()));
            whiteChuchus.forEach(chuchu -> shootParticleLine(serverLevel, chuchu.blockPosition()));


            int blueCount = blueChuchus.size();
            int redCount = redChuchus.size();
            int yellowCount = yellowChuchus.size();
            int whiteCount = whiteChuchus.size();

            player.sendSystemMessage(Component.literal(String.format(
                    "Chuchus Unearthed:\nBlue: %d\nRed: %d\nYellow: %d\nWhite: %d",
                    blueCount, redCount, yellowCount, whiteCount
            )));

            level.playSound(null, usePos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }


    }
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.chuchu_unearther"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.chuchu_jelly"));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void shootParticleLine(ServerLevel level, BlockPos startPos) {
        for (int i = 1; i <= 20; i++) {
            BlockPos currentPos = startPos.above(i);

            // Stop if a block is in the way
            if (!level.isEmptyBlock(currentPos)) {
                break;
            }

            // Spawn particles at the current position
            level.sendParticles(
                    ParticleTypes.GLOW, // Replace with desired particle type
                    currentPos.getX() + 0.5,
                    currentPos.getY() + 0.5,
                    currentPos.getZ() + 0.5,
                    5, // Count
                    0.1, 0.1, 0.1, // Offset
                    0.01 // Speed
            );
        }
    }
}
