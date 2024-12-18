package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.component.ModDataComponentTypes;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.item.client.KorokWandFrondRenderer;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class KorokWandFrond extends Item implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final int RADIUS = 6;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public KorokWandFrond(Properties pProperties) {
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
            private KorokWandFrondRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null) {
                    renderer = new KorokWandFrondRenderer();
                }

                return this.renderer;
            }
        });
    }

    private static final int KNOCKBACK_RADIUS = 6;
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!stack.has(ModDataComponentTypes.FROND_USES.get())) {
            stack.set(ModDataComponentTypes.FROND_USES.get(), 1);
        }
        Player player = (Player) entity;
        ItemStack currentItemStack = player.getItemInHand(player.getUsedItemHand());

        // Create a new ItemStack for the iron sword
        ItemStack newItemStack = new ItemStack(Moditems.KOROK_WAND_EMPTY.get(), 1);

        int currentUses = stack.get(ModDataComponentTypes.FROND_USES.get());


            if (!player.getCooldowns().isOnCooldown(Moditems.KOROK_WAND_FROND.get())) {
                player.getCooldowns().addCooldown(this, 8);
                Level level = player.level();
                if (currentUses == 1) {
                    player.setItemInHand(player.getUsedItemHand(), newItemStack);
                }
                stack.set(ModDataComponentTypes.FROND_USES.get(), Math.max(0, currentUses - 1));
                if (!level.isClientSide) {
                    for (Mob mob : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(KNOCKBACK_RADIUS))) {
                        double d0 = mob.getX() - player.getX();
                        double d1 = mob.getZ() - player.getZ();
                        double pushFactor = 1.4 / Math.sqrt(d0 * d0 + d1 * d1);
                        mob.push(d0 * pushFactor, 0.7, d1 * pushFactor);
                    }
                }
                if (player.isOnFire()) {
                    player.clearFire();
                }
                if (player.fallDistance > 0) {
                    player.push(0, 1, 0);
                }

                player.resetFallDistance();

                if (player.level() instanceof ServerLevel _level) {
                    double x = player.getX();
                    double y = player.getY();
                    double z = player.getZ();
                    _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 500, 0.3, 0.3, 0.3, 0.5);
                    _level.playSeededSound(null, x, y, z,
                            ModSounds.FROND_USE.get(), SoundSource.BLOCKS, 1f, 1f, 0);
                }

            }
            return super.onEntitySwing(stack, entity);
        }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack currentItemStack = player.getItemInHand(hand);



        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.korok_wand_frond.shift_down"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.korok_wand_frond"));
        }
        pTooltipComponents.add(Component.literal("Korok Frond - Uses: " + pStack.get(ModDataComponentTypes.FROND_USES.get())));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}