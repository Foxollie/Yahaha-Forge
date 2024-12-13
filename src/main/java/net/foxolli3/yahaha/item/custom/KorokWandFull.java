package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.entity.custom.FredrickMob;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.item.client.KorokWandFullRenderer;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

public class KorokWandFull extends Item implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public KorokWandFull(Properties pProperties) {
        super(pProperties);
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("animation.korok_wand_block.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
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
            private KorokWandFullRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null) {
                    renderer = new KorokWandFullRenderer();
                }

                return this.renderer;
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        InteractionHand hand = pContext.getHand();
        if(!pContext.getLevel().isClientSide){
            double x = pContext.getClickedPos().getX();
            double y = pContext.getClickedPos().getY();
            double z = pContext.getClickedPos().getZ();
            @NotNull EntityType<FredrickMob> entity = ModEntities.FREDRICK.get();

            if (pContext.getLevel() instanceof ServerLevel _level)
            {
                _level.sendParticles(ParticleTypes.WAX_ON, x + 1, y + 1, z + 1, 200, -1, 1, -1, 1);
                FredrickMob fredrickMob = ModEntities.FREDRICK.get().create(_level);
                if (fredrickMob != null) {
                    fredrickMob.moveTo(x + 0.5,y + 1,z + 0.5,0.0f, 0.0F);
                    fredrickMob.tame(pContext.getPlayer());
                    _level.addFreshEntity(fredrickMob);
                    pContext.getPlayer().setItemInHand(hand, new ItemStack(Moditems.KOROK_WAND_EMPTY.get(), 1));
                    _level.playSeededSound(null, x,y,z,
                            ModSounds.FREDRICK_SPAWN.get(), SoundSource.BLOCKS, 0.4f, 1f,0);
                }


            }


        }
        return InteractionResult.SUCCESS;
    }
}