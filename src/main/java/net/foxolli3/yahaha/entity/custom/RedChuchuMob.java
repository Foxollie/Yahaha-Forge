package net.foxolli3.yahaha.entity.custom;

import net.foxolli3.yahaha.particle.ModParticles;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class RedChuchuMob extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final SimpleContainer inventory = new SimpleContainer(27);

    private boolean shouldPlaySpawnAnimation = false;
    private boolean shouldPlayAttackAnimation = false;
    private Instant lastSeenTime = Instant.now();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RedChuchuMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        //this.hasBecomeVisible = false;
        if(!isVisible()) {
        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        } else {
            this.removeEffect(MobEffects.INVISIBILITY);
        }
    }
    public void setVisible(boolean visible) {
        this.entityData.set(IS_VISIBLE, visible);
        if (isVisible()){
            this.removeEffect((MobEffects.INVISIBILITY));
        } else{
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        }
    }
    public boolean isVisible() {
        return this.entityData.get(IS_VISIBLE);
    }

    private static final EntityDataAccessor<Boolean> IS_VISIBLE =
            SynchedEntityData.defineId(RedChuchuMob.class, EntityDataSerializers.BOOLEAN);
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        setVisible(tag.getBoolean("IsVisible"));

        long timestamp = tag.getLong("LastSeenTime");
        lastSeenTime = Instant.ofEpochMilli(timestamp);
        if(!isVisible()) {
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        } else {
            this.removeEffect(MobEffects.INVISIBILITY);
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        // Save visibility state
        tag.putBoolean("IsVisible", isVisible());
        // Save last seen time
        tag.putLong("LastSeenTime", lastSeenTime.toEpochMilli());
    }


    public static AttributeSupplier setAttributes() {
        return RedChuchuMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f).build();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_VISIBLE, false);
        super.defineSynchedData();
    }

    @Override
    protected void registerGoals() {
        // Add goals to the goalSelector conditionally based on visibility
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this,1.0f,false) {
            @Override
            public boolean canUse() {
                return isVisible() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this,0.2f) {
            @Override
            public boolean canUse() {
                return isVisible() && super.canUse();
            }
        });
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.2D) {
            @Override
            public boolean canUse() {
                return isVisible() && super.canUse();
            }
        });

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return isVisible() && super.canUse();
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this,"controller",0,this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "spawnController", 0, this::spawnPredicate));
    }

    private PlayState spawnPredicate(AnimationState<RedChuchuMob> RedChuchuMobAnimationState) {
        if (shouldPlaySpawnAnimation) {
            RedChuchuMobAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blue_chuchu.spawn", Animation.LoopType.PLAY_ONCE));
            shouldPlaySpawnAnimation = false;
        }
        if (shouldPlayAttackAnimation) {
            RedChuchuMobAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blue_chuchu.attack", Animation.LoopType.PLAY_ONCE));
            shouldPlayAttackAnimation = false;
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blue_chuchu.walking", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blue_chuchu.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    public boolean isPlayerNearby(double radius) {
        List<Player> nearbyPlayers = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(radius));
        return !nearbyPlayers.isEmpty();
    }

    @Override
    public void aiStep() {
        super.aiStep();


        double detectionRadius = 3.0; // Define the radius within which the mob checks for players

        boolean playerNearby = isPlayerNearby(detectionRadius);

        if (playerNearby) {
            if (this.hasEffect(MobEffects.INVISIBILITY)) {
                this.removeEffect(MobEffects.INVISIBILITY);
            }
            if (!isVisible()) {
                this.push(0, 0.5, 0);
                setVisible(true);
                shouldPlaySpawnAnimation = true;
                if (this.level() instanceof ServerLevel _level) {
                    double x = this.getX();
                    double y = this.getY();
                    double z = this.getZ();
                    _level.sendParticles(ModParticles.RED_CHUCHU_BURST_PARTICLES.get(), x, y, z, 5, 0.2, 0.2, 0.2, 0.05f);
                    _level.playSeededSound(null, x, y, z, ModSounds.CHUCHU_SPAWN.get(), SoundSource.HOSTILE, 1f, 1f, 0);
                }
            } else {
                lastSeenTime = Instant.now();
            }

        } else if(!isPlayerNearby(64)) {
            Duration timeSinceLastSeen = Duration.between(lastSeenTime, Instant.now());

            if (isVisible() && timeSinceLastSeen.toSeconds() >= 60) {
                if (!this.hasEffect(MobEffects.INVISIBILITY)) {
                    this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
                }
                setVisible(false);
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }


    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean flag = super.doHurtTarget(entity);
        if (this.level() instanceof ServerLevel _level) {
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();
            _level.playSeededSound(null, x, y, z, ModSounds.CHUCHU_SPAWN.get(), SoundSource.HOSTILE, 1f, 1f, 0);
        }
        if (flag) {
            shouldPlayAttackAnimation = true;
        }
        return flag;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }


    int tickCounter = 0;

    @Override
    public void playerTouch(Player pPlayer) {
        super.playerTouch(pPlayer);
    }

    @Override
    public void tick() {
        if(isVisible()) {
            if (tickCounter % 8 == 0) {
                if (this.level() instanceof ServerLevel _level) {

                    double radius = 0.5; // Radius of the sphere
                    int increment = 60; // Angle increment in degrees

                    for (int theta = 0; theta < 360; theta += increment) { // Horizontal angle (longitude)
                            double radTheta = Math.toRadians(theta);

                            // Spherical to Cartesian conversion
                            double xOffset = radius  * Math.cos(radTheta);
                            double yOffset = radius;
                            double zOffset = radius * Math.sin(radTheta);

                            // Calculate a random delay (up to 3 seconds)
                            int delay = random.nextInt(4);
                            int particleType = random.nextInt(4);
                            // Schedule particle spawning
                            if (delay == 2) {
                                _level.sendParticles(
                                        ParticleTypes.FLAME,
                                        this.getX() + xOffset,
                                        (this.getY() -0.5) + yOffset,
                                        this.getZ() + zOffset,
                                        1,  // Number of particles
                                        0,  // X random offset
                                        0,  // Y random offset
                                        0,   // Z random offset
                                        0.01
                                );
                            }
                        }
                }
            }
            if (tickCounter % 12 == 0) {
                if (this.level() instanceof ServerLevel _level) {
                    _level.sendParticles(ModParticles.RED_CHUCHU_BURST_PARTICLES_SMALL.get(), this.getX(), this.getY() + 0, this.getZ(), 1, 0.2, 0, 0.2, 0);
                }
            }
        }
        super.tick();
    }
}
