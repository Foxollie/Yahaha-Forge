package net.foxolli3.yahaha.entity.custom;

import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HydroFredrickMobOld extends TamableAnimal implements GeoEntity{
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    Player tamePlayer;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public HydroFredrickMobOld(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(HydroFredrickMobOld.class, EntityDataSerializers.BOOLEAN);

    public static AttributeSupplier setAttributes() {
        return HydroFredrickMobOld.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.OXYGEN_BONUS, 20)
                .add(Attributes.SCALE, 1)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY,3).build();
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
        //this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.5f, 10.0f,2.0f));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Creeper.class, true));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.HYDRO_FREDRICK.get().create(pLevel);
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
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(SITTING, false);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public PlayerTeam getTeam() {
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
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }

    private boolean isTrading = false;
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack tradeItemStack;
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        Item itemForTaming = Moditems.KOROK_SEED_POWDER.get();
        Item tradeOutcome = Items.STICK;
        Level level = player.level();

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
                            tamePlayer = player;
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
            if(!isTrading) {
                itemstack.shrink(1);
                int randomTradeInt = randomTrade();
                int tradeAmount = 1;
                if (randomTradeInt >= 0 && randomTradeInt <= 5) {
                    tradeOutcome = Items.EMERALD;
                }
                if (randomTradeInt >= 6 && randomTradeInt <= 10) {
                    tradeOutcome = Items.MANGROVE_LOG;
                    tradeAmount = random.nextInt(1,12);
                }
                if (randomTradeInt >= 11 && randomTradeInt <= 13) {
                    tradeOutcome = Moditems.KOROK_WAND_EMPTY.get();
                }
                if (randomTradeInt >= 14 && randomTradeInt <= 18) {
                    tradeAmount = random.nextInt(1,12);
                    tradeOutcome = Items.LAPIS_LAZULI;
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
                    tradeOutcome = Items.NAUTILUS_SHELL;
                }
                if (randomTradeInt >= 33 && randomTradeInt <= 35) {
                    tradeAmount = random.nextInt(1,4);
                    tradeOutcome = Items.SEA_LANTERN;
                }
                if (randomTradeInt >= 36 && randomTradeInt <= 38) {
                    tradeOutcome = Items.GLOW_INK_SAC;
                    tradeAmount = 2;
                }
                if (randomTradeInt >= 39 && randomTradeInt <= 41) {
                    tradeAmount = random.nextInt(1,2);
                    tradeOutcome = Items.KELP;
                    tradeAmount = random.nextInt(1,32);
                }
                if (randomTradeInt >= 42 && randomTradeInt <= 45) {
                    tradeAmount = random.nextInt(1,3);
                    tradeOutcome = Items.STRING;
                    tradeAmount = random.nextInt(1,4);
                }
                if (randomTradeInt >= 46 && randomTradeInt <= 50) {
                    tradeAmount = random.nextInt(1,3);
                    tradeOutcome = Items.SALMON;
                }
                if (randomTradeInt >= 50 && randomTradeInt <= 53) {
                    tradeAmount = random.nextInt(1,3);
                    tradeOutcome = Items.COD;
                }
                if (randomTradeInt >= 54 && randomTradeInt <= 55) {
                    tradeOutcome = Items.PRISMARINE;
                    tradeAmount = random.nextInt(1,3);
                }
                if (randomTradeInt >= 56 && randomTradeInt <= 57) {
                    tradeOutcome = Moditems.KOROK_FROND.get();
                }
                if (randomTradeInt >= 21 && randomTradeInt <= 22) {
                    tradeOutcome = Moditems.BLUE_CHUCHU_JELLY.get();
                    tradeAmount = random.nextInt(1,3);
                }
                if (randomTradeInt >= 58 && randomTradeInt <= 77) {
                    tradeOutcome = Moditems.BLUE_CHUCHU_JELLY.get();
                    tradeAmount = random.nextInt(1,3);
                }
                if (randomTradeInt >= 78 && randomTradeInt <= 80) {
                    tradeOutcome = Items.TRIDENT;
                }
                tradeItemStack = new ItemStack(tradeOutcome, tradeAmount);
                if (tradeOutcome == Items.TRIDENT) {
                    HolderLookup<Enchantment> enchantmentLookup = this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                    Holder<Enchantment> riptide = enchantmentLookup.getOrThrow(Enchantments.RIPTIDE);
                    int maxDurability = tradeItemStack.getMaxDamage();
                    int randomDurability = random.nextInt(maxDurability / 2, maxDurability); //between half and full durability
                    tradeItemStack.setDamageValue(maxDurability - randomDurability);
                    if (!this.level().isClientSide) {
                        tradeItemStack.enchant(riptide,1);
                    }
                }
                    ItemEntity itementity = new ItemEntity(level, (double) this.getX() , (double) (this.getY() + 1D), (double) this.getZ(), tradeItemStack);
                    for (int i = 0; i < 12; i++){
                        if (level() instanceof ServerLevel _level) {
                            scheduler.schedule(() -> _level.sendParticles(ParticleTypes.WAX_ON, this.getX(), this.getY(), this.getZ(), 5, -0.5, 0.5, -0.5, 1), i / 4, TimeUnit.SECONDS);
                        }
                    }
                    isTrading = true;
                    scheduler.schedule(() -> itementity.setPickUpDelay(50), 3, TimeUnit.SECONDS);
                    scheduler.schedule(() -> itementity.setPos(this.getX(), this.getY(), this.getZ()), 3, TimeUnit.SECONDS);
                    scheduler.schedule(() -> level.addFreshEntity(itementity), 3, TimeUnit.SECONDS);
                    scheduler.schedule(() -> isTrading = false, 3, TimeUnit.SECONDS);
            }
        }
        return super.mobInteract(player, hand);
    }
    private int randomTrade() {
        return RandomSource.createNewThreadLocalInstance().nextInt(80);
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

    private int tickCounter = 0;

    @Override
    public void tick() {
        buffInRadius(this.level(), this.getX(), this.getY(), this.getZ(), 8);
        tickCounter++;
        if (tickCounter % 3 == 0) {
            //action every 10 calls
            if (this.level() instanceof ServerLevel _level) {
                _level.sendParticles(ParticleTypes.UNDERWATER, this.getX(), this.getY()+0.25, this.getZ(), 2, 0.2, 0.2, 0.2, 0.01);
                if (tickCounter % 24 == 0) {
                    _level.sendParticles(ParticleTypes.BUBBLE_POP, this.getX(), this.getY(), this.getZ(), 1, 0.2, 0.2, 0.2, 0.01);
                }

            }
        }
        super.tick();
    }
    public void buffInRadius(Level level, double x, double y, double z, double radius) {
        Vec3 center = new Vec3(x, y, z);
        //iterate through all players
        for (Player entity : level.getEntitiesOfClass(Player.class, new net.minecraft.world.phys.AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius))) {
            double distance = entity.position().distanceTo(center);

            if (distance <= radius) {
                if (entity == tamePlayer) {
                    entity.addEffect((new MobEffectInstance(MobEffects.WATER_BREATHING, 80, 0)));
                }
            }
        }
    }
}
