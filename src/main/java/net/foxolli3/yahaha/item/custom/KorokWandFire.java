package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.entity.custom.FredrickMob;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.item.client.KorokWandBlockRenderer;
import net.foxolli3.yahaha.item.client.KorokWandFireRenderer;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

public class KorokWandFire extends Item implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public KorokWandFire(Properties pProperties) {
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
            private KorokWandFireRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null) {
                    renderer = new KorokWandFireRenderer();
                }

                return this.renderer;
            }
        });
    }

    private int tickCounter = 0; // Counter to track frames

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pIsSelected || pSlotId == -106) {
            if (!pLevel.isClientSide && pLevel instanceof ServerLevel _level) {
                tickCounter++; // Increment the counter every frame
                if (tickCounter >= 20) { // Check if 10 frames have passed
                    _level.sendParticles(ParticleTypes.FLAME, pEntity.getX(), pEntity.getY() + 1, pEntity.getZ(), 1, 0, 0, 0, 0);
                    tickCounter = 0; // Reset the counter
                }
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }
}