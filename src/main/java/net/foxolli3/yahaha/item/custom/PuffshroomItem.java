package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.component.ModDataComponentTypes;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PuffshroomItem extends SwordItem {
    private static final int RADIUS = 6;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PuffshroomItem(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.yahaha.fuse_item"));
        super.appendHoverText(pStack, (TooltipContext) pLevel, pTooltipComponents, pIsAdvanced);
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity.getType() == EntityType.PLAYER) {
            if (!stack.has(ModDataComponentTypes.PUFFSHROOM_USES.get())) {
                stack.set(ModDataComponentTypes.PUFFSHROOM_USES.get(), 1);
            }
            Player player = (Player) entity;
            ItemStack currentItemStack = player.getItemInHand(player.getUsedItemHand());

            // Create a new ItemStack for the iron sword
            ItemStack newItemStack = new ItemStack(Moditems.KOROK_WAND_EMPTY.get(), 1);


            int currentUses = stack.get(ModDataComponentTypes.FROND_USES.get());

            // Set the new item stack in the player's hand
            player.setItemInHand(player.getUsedItemHand(), newItemStack);
            if (!player.getCooldowns().isOnCooldown(Moditems.PUFFSHROOM_SWORD.get())) {
                player.getCooldowns().addCooldown(this, 200);
                Level level = player.level();
                if (currentUses == 1) {
                    player.setItemInHand(player.getUsedItemHand(), newItemStack);
                }
                if (!level.isClientSide) {
                    for (Mob mob : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(RADIUS))) {
                        mob.setNoAi(true);
                        scheduler.schedule(() -> mob.setNoAi(false), 3, TimeUnit.SECONDS);
                    }
                    player.setInvisible(true);
                    scheduler.schedule(() -> player.setInvisible(false), 3, TimeUnit.SECONDS);
                    if (player.level() instanceof ServerLevel _level) {
                        double x = player.getX();
                        double y = player.getY();
                        double z = player.getZ();
                        _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 500, 0.2, 0.2, 0.2, 0.05f);
                        _level.playSeededSound(null, x, y, z,
                                ModSounds.PUFFSHROOM.get(), SoundSource.BLOCKS, 1f, 1f, 0);
                        scheduler.schedule(() -> _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX(), player.getY(), player.getZ(), 25, 0.5, 0.5, 0.5, 0.02f), 3, TimeUnit.SECONDS);
                        for (int i = 0; i < 8; i++) {
                            scheduler.schedule(() -> _level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 250, 0.5, 0.5, 0.5, 0.02f), i/2, TimeUnit.SECONDS);
                        }
                    }
                }
            }
        }
        return super.onEntitySwing(stack, entity);
    }
}
