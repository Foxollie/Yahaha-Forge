package net.foxolli3.yahaha.item.custom;

import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.entity.custom.CryoFredrickMob;
import net.foxolli3.yahaha.entity.custom.FredrickMob;
import net.foxolli3.yahaha.entity.custom.HydroFredrickMob;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.item.client.KorokWandFullRenderer;
import net.foxolli3.yahaha.item.client.KorokWandIceFullRenderer;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class KorokWandIceFull extends Item implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public KorokWandIceFull(Properties pProperties) {
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
        return RenderUtils.getCurrentTick();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private KorokWandIceFullRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null) {
                    renderer = new KorokWandIceFullRenderer();
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

    private final Map<BlockPos, Integer> spawnAttemptTracker = new HashMap<>();



    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        InteractionHand hand = pContext.getHand();
        if (!pContext.getLevel().isClientSide) {
            ServerLevel _level = (ServerLevel) pContext.getLevel();
            BlockPos clickedPos = pContext.getClickedPos().above();
            Player player = pContext.getPlayer();

            // Spawn particles at the clicked position


            // Try finding a valid spawn position
            BlockPos validPos = findValidSpawnPosition(clickedPos, _level);

            if (validPos != null) {
                // Valid position found, spawn Fredrick immediately
                spawnFredrick(validPos, _level, player, hand);
                spawnAttemptTracker.remove(clickedPos); // Reset tracker for this position
            } else {
                // Handle messaging and forced spawn logic after failed attempts
                int attempts = spawnAttemptTracker.getOrDefault(clickedPos, 0);

                if (attempts < 10) {
                    player.sendSystemMessage(Component.literal("Cannot spawn here!"));
                } else if (attempts == 10) {
                    player.sendSystemMessage(Component.literal("..."));
                } else if (attempts == 11) {
                    player.sendSystemMessage(Component.literal("......"));
                } else if (attempts == 12) {
                    player.sendSystemMessage(Component.literal("........."));
                } else if (attempts == 13) {
                    player.sendSystemMessage(Component.literal("............"));
                } else if (attempts == 14) {
                    player.sendSystemMessage(Component.literal("...yahaha?"));
                    spawnFredrick(clickedPos, _level, player, hand); // Force spawn Fredrick
                    spawnAttemptTracker.remove(clickedPos); // Reset tracker for this position
                }

                spawnAttemptTracker.put(clickedPos, attempts + 1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void spawnParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(ParticleTypes.SNOWFLAKE, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 300, 1, 1, 1, 0);
       // level.sendParticles(ParticleTypes.WAX_ON, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 100, 1, 1, 1, 0);
    }

    private BlockPos findValidSpawnPosition(BlockPos startPos, ServerLevel level) {
        for (int dy = 0; dy <= 2; dy++) {
            BlockPos testPos = startPos.above(dy);
            BlockState blockState = level.getBlockState(testPos);
            BlockState blockAboveState = level.getBlockState(testPos.above());
            BlockState blockBelowState = level.getBlockState(testPos.below());

            boolean isAir = blockState.isAir();
            boolean hasSpaceAbove = blockAboveState.isAir();
            boolean hasSolidBase = blockBelowState.isCollisionShapeFullBlock(level, testPos.below());

            if (isAir && hasSpaceAbove && hasSolidBase) {
                return testPos;
            }
        }
        return null;
    }

    private void spawnFredrick(BlockPos pos, ServerLevel level, Player player, InteractionHand hand) {
        CryoFredrickMob fredrickMob = ModEntities.CRYO_FREDRICK.get().create(level);
        if (fredrickMob != null) {
            spawnParticles(level, pos);
            fredrickMob.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0f, 0.0F);
            fredrickMob.tame(player);
            level.addFreshEntity(fredrickMob);

            // Replace item in hand
            player.setItemInHand(hand, new ItemStack(Moditems.KOROK_WAND_EMPTY.get(), 1));

            // Play sound
            level.playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.FREDRICK_SPAWN.get(), SoundSource.BLOCKS, 0.4f, 1f, 0);
        }
    }
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.korok_wand_ice_full"));
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.yahaha.chuchu_jelly"));
        }
        pTooltipComponents.add(Component.literal("§3FREDRICK §7[CRYO]§r"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}