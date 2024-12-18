package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.component.ModDataComponentTypes;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.item.client.KorokWandBlockRenderer;
import net.foxolli3.yahaha.item.client.KorokWandPuffshroomRenderer;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class KorokWandPuffshroom extends Item implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final int RADIUS = 6;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public KorokWandPuffshroom(Properties pProperties) {
        super(pProperties);
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.korok_wand_block.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private KorokWandPuffshroomRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null) {
                    renderer = new KorokWandPuffshroomRenderer();
                }

                return this.renderer;
            }
        });
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


            int currentUses = stack.get(ModDataComponentTypes.PUFFSHROOM_USES.get());

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
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.korok_wand_puffshroom.shift_down"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.korok_wand_puffshroom"));
        }
        pTooltipComponents.add(Component.literal("Puffshroom - Uses: " + pStack.get(ModDataComponentTypes.PUFFSHROOM_USES.get())));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}