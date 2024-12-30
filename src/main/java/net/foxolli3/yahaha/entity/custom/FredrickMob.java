package net.foxolli3.yahaha.entity.custom;

import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.screen.FredrickStatScreen;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FredrickMob extends TamableAnimal implements GeoEntity, NeutralMob {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public Player tamePlayer;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public FredrickMob(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(FredrickMob.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LUCK =
            SynchedEntityData.defineId(FredrickMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> FREDRICK_DAMAGE =
            SynchedEntityData.defineId(FredrickMob.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> BUFF_RADIUS =
            SynchedEntityData.defineId(FredrickMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> EFFECT_LEVEL =
            SynchedEntityData.defineId(FredrickMob.class, EntityDataSerializers.INT);

    public static AttributeSupplier setAttributes() {
        return FredrickMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 1)
                .add(Attributes.ATTACK_SPEED, 0.2f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0)
                .add(Attributes.ARMOR, 8.0)
                .add(Attributes.BURNING_TIME, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.2F).build();
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
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.5f, 10.0f,2.0f));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));


        OwnerHurtByTargetGoal ownerHurtByTargetGoal = new OwnerHurtByTargetGoal(this);
        OwnerHurtTargetGoal ownerHurtTargetGoal = new OwnerHurtTargetGoal(this);
        HurtByTargetGoal hurtByTargetGoal = new HurtByTargetGoal(this).setAlertOthers();
        NearestAttackableTargetGoal<Player> nearestAttackableTargetGoal = new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt);

            this.targetSelector.addGoal(5, ownerHurtByTargetGoal);
            this.targetSelector.addGoal(6, ownerHurtTargetGoal);
            this.targetSelector.addGoal(7, hurtByTargetGoal);
            this.targetSelector.addGoal(8, nearestAttackableTargetGoal);
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
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(SITTING, false);
        pBuilder.define(LUCK, 0);
        pBuilder.define(BUFF_RADIUS, 0);
        pBuilder.define(FREDRICK_DAMAGE, 0f);
        pBuilder.define(EFFECT_LEVEL, 1);
    }
    public int testLuck(int x) {
        if (x < 10) {
            return randomLuck() < (x * 10) ? 2 : 1;
        } else if (x < 20) {
            return randomLuck() < ((x - 10) * 10) ? 3 : 2;
        } else if (x <= 30) {
            return randomLuck() < ((x - 20) * 10) ? 4 : 3;
        }
        return 1;
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }
    public void setLuck(int luck) {
        this.entityData.set(LUCK, luck);
    }
    public void setFredrickDamage(float damage) {
        this.entityData.set(FREDRICK_DAMAGE, damage);
    }
    public void setBuffRadius(int buffRadius) {
        this.entityData.set(BUFF_RADIUS, buffRadius);;
    }
    public void setEffectLevel(int effectLevel) {
        this.entityData.set(EFFECT_LEVEL, effectLevel);;
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }
    public int luck() {
        return this.entityData.get(LUCK);
    }
    public float fredrick_damage() {
        return this.entityData.get(FREDRICK_DAMAGE);
    }
    public int buffRadius() {
        return this.entityData.get(BUFF_RADIUS);
    }
    public int effectLevel() {
        return this.entityData.get(EFFECT_LEVEL);
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
        setBuffRadius(tag.getInt("buffRadius"));
        setEffectLevel(tag.getInt("effectLevel"));
        setFredrickDamage(tag.getInt("attackDamage"));
        setLuck(tag.getInt("luck"));
    }
    String tAnimationState(String tAnimationState) {
        return this.getAnimatableInstanceCache().toString();
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
        tag.putInt("buffRadius", this.buffRadius());
        tag.putInt("effectLevel", this.effectLevel());
        tag.putFloat("attackDamage", this.fredrick_damage());
        tag.putInt("luck", this.luck());
    }



    private boolean isTrading = false;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            if (player == tamePlayer) {
                ItemStack itemstack = player.getItemInHand(hand);
                Item item = itemstack.getItem();
                Item itemForTaming = Items.APPLE;
                Item tradeOutcome = Items.STICK;
                Level level = player.level();

                if (item == Moditems.KOROK_SEED.get() && !this.level().isClientSide) {

                    {
                        if (this.getTeam() != null && !this.getTeam().equals(player.getTeam())) {
                            return InteractionResult.FAIL;
                        }

                        if (!isTrading) {
                            for (int o = 0; o < testLuck(luck()); o++) {
                                itemstack.shrink(1);
                                int randomTradeInt = randomTrade();
                                int tradeAmount = 1;
                                if (randomTradeInt >= 0 && randomTradeInt <= 5) {
                                    tradeOutcome = Items.EMERALD;
                                }
                                if (randomTradeInt >= 6 && randomTradeInt <= 10) {
                                    tradeOutcome = Items.SPRUCE_LOG;
                                    tradeAmount = random.nextInt(1, 12);
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
                                    tradeOutcome = Items.ANDESITE;
                                    tradeAmount = random.nextInt(1, 12);
                                }
                                if (randomTradeInt >= 36 && randomTradeInt <= 41) {
                                    tradeOutcome = Moditems.PUFFSHROOM.get();
                                    tradeAmount = random.nextInt(1, 3);
                                }
                                if (randomTradeInt >= 42 && randomTradeInt <= 45) {
                                    tradeOutcome = Items.GUNPOWDER;
                                }
                                if (randomTradeInt >= 46 && randomTradeInt <= 50) {
                                    tradeOutcome = Items.SALMON;
                                    tradeAmount = random.nextInt(1, 3);
                                }
                                if (randomTradeInt >= 50 && randomTradeInt <= 53) {
                                    tradeOutcome = Items.COD;
                                    tradeAmount = random.nextInt(1, 3);
                                }
                                if (randomTradeInt >= 54 && randomTradeInt <= 55) {
                                    tradeOutcome = Items.NETHER_BRICK;
                                    tradeAmount = 3;
                                }
                                if (randomTradeInt >= 56 && randomTradeInt <= 57) {
                                    tradeOutcome = Moditems.KOROK_FROND.get();
                                }
                                if (randomTradeInt >= 21 && randomTradeInt <= 22) {
                                    tradeOutcome = Moditems.BLUE_CHUCHU_JELLY.get();
                                    tradeAmount = random.nextInt(1, 3);
                                }
                                if (randomTradeInt >= 58 && randomTradeInt <= 59) {
                                    tradeOutcome = Moditems.RED_CHUCHU_JELLY.get();
                                    tradeAmount = random.nextInt(1, 3);
                                }
                                if (randomTradeInt >= 60 && randomTradeInt <= 61) {
                                    tradeOutcome = Moditems.YELLOW_CHUCHU_JELLY.get();
                                    tradeAmount = random.nextInt(1, 3);
                                }
                                if (randomTradeInt >= 62 && randomTradeInt <= 63) {
                                    tradeOutcome = Moditems.WHITE_CHUCHU_JELLY.get();
                                    tradeAmount = random.nextInt(1, 3);
                                }


                                ItemEntity itementity = new ItemEntity(level, (double) this.getX(), (double) (this.getY() + 1D), (double) this.getZ(), new ItemStack(tradeOutcome, tradeAmount));
                                for (int i = 0; i < 12; i++) {
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
                    }
                } else if (item == Moditems.ORANGE_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
                        double current = this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
                        if (current < 0.49) {
                            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(current + 0.01);
                            current = this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
                            player.sendSystemMessage(Component.literal("Movement Speed Increased to " + decimalFormat.format((current * 100 - 20))));
                            itemstack.shrink(1);
                        } else {
                            player.sendSystemMessage(Component.literal("Movement Speed is at Max Value!"));
                        }
                    }
                } else if (item == Moditems.BLUE_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (buffRadius() < 120) {
                        this.setBuffRadius(buffRadius() + 10);
                        player.sendSystemMessage(Component.literal("Effect Area Increased to " + buffRadius()));
                        itemstack.shrink(1);
                    } else {
                        player.sendSystemMessage(Component.literal("Effect Area is at Max Value!"));
                    }
                } else if (item == Moditems.WHITE_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (this.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
                        if (fredrick_damage() < 40) {
                            setFredrickDamage(fredrick_damage() + 0.5f);
                            player.sendSystemMessage(Component.literal("Attack Increased to " + decimalFormat.format((fredrick_damage()))));
                            itemstack.shrink(1);
                        } else {
                            player.sendSystemMessage(Component.literal("Attack Damage is at Max Value!"));
                        }
                    }
                } else if (item == Moditems.DARK_BLUE_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (this.getAttribute(Attributes.SCALE) != null) {
                        double current = this.getAttribute(Attributes.SCALE).getBaseValue();
                        if (current < 16) {
                            this.getAttribute(Attributes.SCALE).setBaseValue(current * 1.05);
                            current = this.getAttribute(Attributes.SCALE).getBaseValue();
                            player.sendSystemMessage(Component.literal("Size Increased to " + decimalFormat.format(current)));
                            itemstack.shrink(1);
                        } else {
                            player.sendSystemMessage(Component.literal("Size is at Max Value!"));
                        }
                    }
                } else if (item == Moditems.GREEN_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (luck() < 30) {
                        this.setLuck(this.luck() + 1);
                        player.sendSystemMessage(Component.literal("Trade Luck Increased to " + this.luck()));
                        itemstack.shrink(1);
                    } else {
                        player.sendSystemMessage(Component.literal("Luck is at Max Value"));
                    }
                } else if (item == Moditems.RED_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (this.getAttribute(Attributes.BURNING_TIME) != null) {
                        double current = this.getAttribute(Attributes.BURNING_TIME).getBaseValue();
                        if (current > 0.01) {
                            this.getAttribute(Attributes.BURNING_TIME).setBaseValue(current - 0.1);
                            current = this.getAttribute(Attributes.BURNING_TIME).getBaseValue();
                            player.sendSystemMessage(Component.literal("Burn Time Reduced to " + decimalFormat.format(current)));
                            itemstack.shrink(1);
                        } else {
                            player.sendSystemMessage(Component.literal("Burn Time is at Minimum Value!"));
                            this.getAttribute(Attributes.BURNING_TIME).setBaseValue(0);
                        }
                    }
                } else if (item == Moditems.BLACK_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (this.getAttribute(Attributes.ATTACK_SPEED) != null) {
                        if (effectLevel() < 3) {
                            this.setEffectLevel(effectLevel() + 1);
                            player.sendSystemMessage(Component.literal("Effect Level Increased to " + effectLevel()));
                            itemstack.shrink(1);
                        } else {
                            player.sendSystemMessage(Component.literal("Effect Level is at Max Value!"));
                        }
                    }
                } else if (item == Moditems.PURPLE_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (this.getAttribute(Attributes.MAX_HEALTH) != null) {
                        double current = this.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
                        if (current < 400) {
                            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(current + 1);
                            current = this.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
                            player.sendSystemMessage(Component.literal("Max Health Increased to " + current));
                            itemstack.shrink(1);
                        } else {
                            player.sendSystemMessage(Component.literal("Max Health is at Max Value!"));
                        }
                    }
                } else if (item == Moditems.OBSIDIAN_KOROK_SEED.get() && !this.level().isClientSide) {
                    if (this.getAttribute(Attributes.ARMOR) != null) {
                        double current = this.getAttribute(Attributes.ARMOR).getBaseValue();
                        if (current < 30) {
                            this.getAttribute(Attributes.ARMOR).setBaseValue(current + 1);
                            current = this.getAttribute(Attributes.ARMOR).getBaseValue();
                            player.sendSystemMessage(Component.literal("Defense Increased to " + current));
                            itemstack.shrink(1);
                        } else {
                            player.sendSystemMessage(Component.literal("Defense is at Max Value!"));
                        }
                    }
                } else if ((item == Items.IRON_NUGGET)) {
                    if (this.getHealth() < this.getMaxHealth()) {
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                        this.setHealth(this.getHealth() + 2);
                        this.spawnTamingParticles(true);
                    }
                } else if ((item == Items.GOLD_NUGGET)) {
                    if (this.getHealth() < this.getMaxHealth()) {
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                        this.setHealth(this.getHealth() + 6);
                        this.spawnTamingParticles(true);
                    }
                } else if ((item == Items.DIAMOND)) {
                    if (this.getHealth() < this.getMaxHealth()) {
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                        this.setHealth(this.getMaxHealth());
                        this.spawnTamingParticles(true);
                    }
                }else if (player.isCrouching()) {
                }  else {

                    if (item == itemForTaming && !isTame() && !this.level().isClientSide) {
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
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
        if (pPlayer.isCrouching()) {
            if (pPlayer.level().isClientSide) {
                if (this.getOwner() != null && this.getDisplayName() != null) {
                    Minecraft.getInstance().setScreen(new FredrickStatScreen(Component.literal(this.getDisplayName().getString() + " (" + this.getOwner().getName().getString() + ")"), this, this.luck(), this.buffRadius(), this, fredrick_damage(), effectLevel()));
                }
            }
        }
        return super.interactAt(pPlayer, pVec, pHand);
    }

    @Override
    public void tick() {
        buffInRadius(this.level(), this.getX(), this.getY(), this.getZ(), buffRadius());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(fredrick_damage());
        tamePlayer = (Player) this.getOwner();
        super.tick();
    }

    private int randomTrade() {
        return RandomSource.createNewThreadLocalInstance().nextInt(63);
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
    private static int randomLuck() {
        return RandomSource.createNewThreadLocalInstance().nextInt(1,10);
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

    public void buffInRadius(Level level, double x, double y, double z, double radius) {
        Vec3 center = new Vec3(x, y, z);
        //iterate through all players
        for (Player entity : level.getEntitiesOfClass(Player.class, new net.minecraft.world.phys.AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius))) {
            double distance = entity.position().distanceTo(center);

            if (distance <= radius) {
                if (entity == tamePlayer) {
                    entity.addEffect((new MobEffectInstance(MobEffects.REGENERATION, 85,effectLevel()-1)));
                }
            }
        }
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {

    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }
}
