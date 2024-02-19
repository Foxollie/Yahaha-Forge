package net.foxolli3.yahaha.entity.custom;

import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import javax.xml.stream.Location;
public class FredrickMob extends TamableAnimal implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public FredrickMob(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(FredrickMob.class, EntityDataSerializers.BOOLEAN);

    public static AttributeSupplier setAttributes() {
        return FredrickMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f).build();
    }

    public void moveTo(double pX, double pY, double pZ, float pYRot, float pXRot) {
        this.setPosRaw(pX, pY, pZ);
        this.setYRot(pYRot);
        this.setXRot(pXRot);
        this.setOldPosAndRot();
        this.reapplyPosition();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.5f, 10.0f,2.0f,false));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Creeper.class, true));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.FREDRICK.get().create(pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"controller",0,this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.walking", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
            if(this.isSitting()){
                tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.sitting", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }

        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return true;
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        Item itemForTaming = Moditems.KOROK_SEED_POWDER.get();
        Item tradeOutcome = Items.STICK;
        Level  level = player.level();

        if (item != Moditems.KOROK_SEED.get()) {

            if (item == itemForTaming && !isTame()) {
                if (this.level().isClientSide) {
                    return InteractionResult.CONSUME;
                } else {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    if (!ForgeEventFactory.onAnimalTame(this, player)) {
                        if (!this.level().isClientSide) {
                            super.tame(player);
                            this.navigation.recomputePath();
                            this.setTarget(null);
                            this.level().broadcastEntityEvent(this, (byte) 7);
                            setSitting(true);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        if (isTame() && !this.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }
    } else {
            if(!player.getCooldowns().isOnCooldown(Moditems.KOROK_SEED.get())) {
                itemstack.shrink(1);
                int randomTradeInt = randomTrade();
                int tradeAmount = 1;
                if (randomTradeInt >= 0 && randomTradeInt <= 5) {
                    tradeOutcome = Items.EMERALD;
                }
                if (randomTradeInt >= 6 && randomTradeInt <= 10) {
                    tradeOutcome = Items.CHORUS_FRUIT;
                    tradeAmount = 3;
                }
                if (randomTradeInt >= 11 && randomTradeInt <= 13) {
                    tradeOutcome = Moditems.KOROK_WAND_EMPTY.get();
                }
                if (randomTradeInt >= 14 && randomTradeInt <= 18) {
                    tradeOutcome = Items.GOLD_INGOT;
                }
                if (randomTradeInt >= 19 && randomTradeInt <= 20) {
                    tradeOutcome = Moditems.KAZOO_VIEW_HIGHWAY_DISC.get();
                }
                if (randomTradeInt >= 23 && randomTradeInt <= 24) {
                    tradeOutcome = Moditems.PIRANHA_PLANTS_ON_PARADE_KAZOO_COVER_MUSIC_DISC.get();
                }
                if (randomTradeInt >= 25 && randomTradeInt <= 26) {
                    tradeOutcome = Moditems.SUIKA_GAME_THEME_KAZOO_COVER_MUSIC_DISC.get();
                }
                if (randomTradeInt >= 29 && randomTradeInt <= 32) {
                    tradeOutcome = Items.DIAMOND;
                }
                if (randomTradeInt >= 33 && randomTradeInt <= 35) {
                    tradeOutcome = Items.ALLIUM;
                }
                if (randomTradeInt >= 36 && randomTradeInt <= 38) {
                    tradeOutcome = Items.GLOW_INK_SAC;
                    tradeAmount = 2;
                }
                if (randomTradeInt >= 39 && randomTradeInt <= 41) {
                    tradeOutcome = Items.BLAZE_POWDER;
                }
                if (randomTradeInt >= 42 && randomTradeInt <= 45) {
                    tradeOutcome = Items.GUNPOWDER;
                }
                if (randomTradeInt >= 46 && randomTradeInt <= 50) {
                    tradeOutcome = Items.SALMON;
                }
                if (randomTradeInt >= 50 && randomTradeInt <= 53) {
                    tradeOutcome = Items.COD;
                }
                if (randomTradeInt >= 54 && randomTradeInt <= 55) {
                    tradeOutcome = Items.NETHER_BRICK;
                    tradeAmount = 3;
                }
                if (randomTradeInt >= 56 && randomTradeInt <= 57) {
                    tradeOutcome = Moditems.KOROK_FROND.get();
                }
                if (randomTradeInt >= 21 && randomTradeInt <= 22) {
                    tradeOutcome = Items.BAMBOO;
                    tradeAmount = 12;
                }
                player.getCooldowns().addCooldown(Moditems.KOROK_SEED.get(), 10);
                    ItemEntity itementity = new ItemEntity(level, (double) player.getX() , (double) (player.getY() + 1D), (double) player.getZ(), new ItemStack(tradeOutcome, tradeAmount));
                    itementity.setPickUpDelay(50);
                    level.addFreshEntity(itementity);
            }
        }
        return super.mobInteract(player, hand);
    }
    private int randomTrade() {
        return RandomSource.createNewThreadLocalInstance().nextInt(58);
    }
    protected SoundEvent getAmbientSound() {
        int randomSoundInt = randomSound();
        if (randomSoundInt == 1) {
            return ModSounds.YAHAHA.get();
        } else if (randomSoundInt == 2) {
            return ModSounds.KOROK_QUESTION_1.get();
        } else {
            return ModSounds.KOROK_QUESTION_2.get();
        }
    }
    private int randomSound() {
        return RandomSource.createNewThreadLocalInstance().nextInt(3);
    }
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.FREDRICK_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.FREDRICK_DEATH.get();
    }

    protected float getSoundVolume() {
        return 0.75F;
    }
}
