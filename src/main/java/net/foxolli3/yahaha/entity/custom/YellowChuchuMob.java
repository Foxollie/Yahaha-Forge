package net.foxolli3.yahaha.entity.custom;

import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.particle.ModParticles;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class YellowChuchuMob extends Monster implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final SimpleContainer inventory = new SimpleContainer(27);

    private boolean shouldPlaySpawnAnimation = false;
    private boolean shouldPlayAttackAnimation = false;
    private boolean isVisible = false;
    private Instant lastSeenTime = Instant.now();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public YellowChuchuMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        //this.hasBecomeVisible = false;
        if (!isVisible) {
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        } else {
            this.removeEffect(MobEffects.INVISIBILITY);
        }
    }


    private boolean hasBecomeVisible;

    public static AttributeSupplier setAttributes() {
        return YellowChuchuMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f).build();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
    }

    @Override
    protected void registerGoals() {
        // Add goals to the goalSelector conditionally based on visibility
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0f, false) {
            @Override
            public boolean canUse() {
                return isVisible && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.2f) {
            @Override
            public boolean canUse() {
                return isVisible && super.canUse();
            }
        });
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.2D) {
            @Override
            public boolean canUse() {
                return isVisible && super.canUse();
            }
        });

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return isVisible && super.canUse();
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "spawnController", 0, this::spawnPredicate));
    }

    private PlayState spawnPredicate(AnimationState<YellowChuchuMob> YellowChuchuMobAnimationState) {
        if (shouldPlaySpawnAnimation) {
            YellowChuchuMobAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blue_chuchu.spawn", Animation.LoopType.PLAY_ONCE));
            shouldPlaySpawnAnimation = false;
        }
        if (shouldPlayAttackAnimation) {
            YellowChuchuMobAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.blue_chuchu.attack", Animation.LoopType.PLAY_ONCE));
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
            if (!isVisible) {
                this.push(0, 0.5, 0);
                isVisible = true;
                shouldPlaySpawnAnimation = true;
                if (this.level() instanceof ServerLevel _level) {
                    double x = this.getX();
                    double y = this.getY();
                    double z = this.getZ();
                    _level.sendParticles(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES.get(), x, y, z, 5, 0.2, 0.2, 0.2, 0.05f);
                    _level.playSeededSound(null, x, y, z, ModSounds.CHUCHU_SPAWN.get(), SoundSource.HOSTILE, 1f, 1f, 0);
                }
            } else {
                lastSeenTime = Instant.now();
            }

        } else if (!isPlayerNearby(64)) {
            Duration timeSinceLastSeen = Duration.between(lastSeenTime, Instant.now());

            if (isVisible && timeSinceLastSeen.toSeconds() >= 60) {
                if (!this.hasEffect(MobEffects.INVISIBILITY)) {
                    this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
                }
                isVisible = false; // Set the visibility flag to false
            }
        }

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public PlayerTeam getTeam() {
        return super.getTeam();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // Read visibility state
        isVisible = tag.getBoolean("IsVisible");

        // Read last seen time
        long timestamp = tag.getLong("LastSeenTime");
        lastSeenTime = Instant.ofEpochMilli(timestamp);
        if (!isVisible) {
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
        } else {
            this.removeEffect(MobEffects.INVISIBILITY);
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        // Save visibility state
        tag.putBoolean("IsVisible", isVisible);
        // Save last seen time
        tag.putLong("LastSeenTime", lastSeenTime.toEpochMilli());
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

    int tickCounter = 0;
    private int superTickCounter = 0;

    @Override
    public void tick() {
        tickCounter++;
        // Get the current position of the entity
        int x = (int) this.getX();
        int y = (int) this.getY();
        int z = (int) this.getZ();

// Flag to check if any metallic block is found
        boolean foundMetallic = false;

// Iterate through the surrounding blocks in a 3x3 area
        if (isVisible) {
            for (int dx = -2; dx <= 1; dx++) { // X direction (-1, 0, 1)
                for (int dz = -2; dz <= 1; dz++) { // Z direction (-1, 0, 1)
                    for (int dy = -1; dy <= 1; dy++) { // Z direction (-1, 0, 1)
                        // Get the position of the surrounding block
                        BlockPos pos = new BlockPos(x + dx, y + dy, z + dz);
                        BlockState blockState = this.level().getBlockState(pos);
                        Block block = blockState.getBlock();

                        // Check if the block is metallic
                        if (isMetallicBlock(block) || isBlockWaterlogged(blockState)) {
                            supercharge();
                            foundMetallic = true;
                            break; // Exit the loop once a metallic block is found
                        }
                    }
                }
                if (foundMetallic) {
                    break; // Exit the outer loop if a metallic block is found
                }
            }

// If no metallic block was found, execute the else logic
            if (!foundMetallic) {
                if (tickCounter % 8 == 0) {
                    if (this.level() instanceof ServerLevel _level) {

                        double radius = 1; // Radius of the sphere
                        int increment = 60; // Angle increment in degrees

                        for (int theta = 0; theta < 360; theta += increment) { // Horizontal angle (longitude)
                            for (int phi = 0; phi <= 180; phi += increment) { // Vertical angle (latitude)
                                double radTheta = Math.toRadians(theta);
                                double radPhi = Math.toRadians(phi);

                                // Spherical to Cartesian conversion
                                double xOffset = radius * Math.sin(radPhi) * Math.cos(radTheta);
                                double yOffset = radius * Math.cos(radPhi);
                                double zOffset = radius * Math.sin(radPhi) * Math.sin(radTheta);

                                // Calculate a random delay (up to 3 seconds)
                                int delay = random.nextInt(4);
                                int particleType = random.nextInt(4);
                                // Schedule particle spawning
                                if (delay == 2) {
                                    _level.sendParticles(
                                            ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_SMALL.get(),
                                            this.getX() + xOffset,
                                            (this.getY() + 0.3) + yOffset,
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
                }
                if (tickCounter % 8 == 0) {
                    if (this.level() instanceof ServerLevel _level) {
                        _level.sendParticles(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES.get(), this.getX(), this.getY() + 0.2, this.getZ(), 1, 0, 0, 0., 0);
                    }
                }
            }
        }
        super.tick();
    }

    @Override
    public void playerTouch(Player pPlayer) {
        if (isVisible) {
            pPlayer.hurt(pPlayer.damageSources().mobAttack(this), 2);
            for (int i = 0; i <= 64; i++) {
                if (i % 8 == 0) {
                    if (this.level() instanceof ServerLevel _level) {

                        double radius = 1.5; // Radius of the sphere
                        int increment = 30; // Angle increment in degrees

                        for (int theta = 0; theta < 360; theta += increment) { // Horizontal angle (longitude)
                            for (int phi = 0; phi <= 180; phi += increment) { // Vertical angle (latitude)
                                double radTheta = Math.toRadians(theta);
                                double radPhi = Math.toRadians(phi);

                                // Spherical to Cartesian conversion
                                double xOffset = radius * Math.sin(radPhi) * Math.cos(radTheta);
                                double yOffset = radius * Math.cos(radPhi);
                                double zOffset = radius * Math.sin(radPhi) * Math.sin(radTheta);

                                // Calculate a random delay (up to 3 seconds)
                                int delay = random.nextInt(4);
                                int particleType = random.nextInt(4);
                                // Schedule particle spawning
                                if (delay == 2) {
                                    _level.sendParticles(
                                            ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_SMALL.get(),
                                            pPlayer.getX() + xOffset,
                                            (pPlayer.getY() + 1) + yOffset,
                                            pPlayer.getZ() + zOffset,
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
                }
            }
            super.playerTouch(pPlayer);
        }
    }

    public boolean isMetallicBlock(Block block) {

        if (block == Blocks.IRON_BLOCK || block == Blocks.GOLD_BLOCK || block == Blocks.COPPER_BLOCK
                || block == Blocks.NETHERITE_BLOCK || block == Blocks.IRON_ORE || block == Blocks.GOLD_ORE
                || block == Blocks.COPPER_ORE || block == Blocks.ANVIL || block == Blocks.CHIPPED_ANVIL
                || block == Blocks.DAMAGED_ANVIL || block == Blocks.IRON_DOOR
                || block == Blocks.IRON_TRAPDOOR || block == Blocks.EXPOSED_COPPER || block == Blocks.WEATHERED_COPPER || block == Blocks.OXIDIZED_COPPER
                || block == Blocks.WAXED_EXPOSED_COPPER
                || block == Blocks.RAW_COPPER_BLOCK
                || block == Blocks.RAW_IRON_BLOCK
                || block == Blocks.DEEPSLATE_IRON_ORE
                || block == Blocks.DEEPSLATE_COPPER_ORE
                || block == Blocks.WAXED_WEATHERED_COPPER || block == Blocks.WAXED_OXIDIZED_COPPER
                || block == Blocks.CUT_COPPER || block == Blocks.EXPOSED_CUT_COPPER || block == Blocks.WEATHERED_CUT_COPPER
                || block == Blocks.OXIDIZED_CUT_COPPER || block == Blocks.WAXED_CUT_COPPER || block == Blocks.WAXED_EXPOSED_CUT_COPPER
                || block == Blocks.WAXED_WEATHERED_CUT_COPPER || block == Blocks.WAXED_OXIDIZED_CUT_COPPER
                || block == Blocks.CUT_COPPER_SLAB || block == Blocks.EXPOSED_CUT_COPPER_SLAB || block == Blocks.WEATHERED_CUT_COPPER_SLAB
                || block == Blocks.OXIDIZED_CUT_COPPER_SLAB || block == Blocks.WAXED_CUT_COPPER_SLAB || block == Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB
                || block == Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB || block == Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB
                || block == Blocks.CUT_COPPER_STAIRS || block == Blocks.EXPOSED_CUT_COPPER_STAIRS || block == Blocks.WEATHERED_CUT_COPPER_STAIRS
                || block == Blocks.OXIDIZED_CUT_COPPER_STAIRS || block == Blocks.WAXED_CUT_COPPER_STAIRS || block == Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS
                || block == Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS || block == Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS) {
            return true;
        }
        return false;
    }

    public boolean isBlockWaterlogged(BlockState blockState) {
        return (blockState.hasProperty(BlockStateProperties.WATERLOGGED) && blockState.getValue(BlockStateProperties.WATERLOGGED) || blockState.getBlock() == Blocks.WATER);
    }

    private void supercharge() {
        if (isVisible) {
            hurtInRadius(this.level(), this.getX(), this.getY(), this.getZ(), 3, 3f);
            if (superTickCounter % 8 == 0 || superTickCounter == 0) {
                if (this.level() instanceof ServerLevel _level) {

                    double radius = 2.1; // Radius of the sphere
                    int increment = 40; // Angle increment in degrees

                    for (int theta = 0; theta < 360; theta += increment) { // Horizontal angle (longitude)
                        for (int phi = 0; phi <= 180; phi += increment) { // Vertical angle (latitude)
                            double radTheta = Math.toRadians(theta);
                            double radPhi = Math.toRadians(phi);

                            // Spherical to Cartesian conversion
                            double xOffset = radius * Math.sin(radPhi) * Math.cos(radTheta);
                            double yOffset = radius * Math.cos(radPhi);
                            double zOffset = radius * Math.sin(radPhi) * Math.sin(radTheta);

                            // Calculate a random delay (up to 3 seconds)
                            int delay = random.nextInt(4);
                            int particleType = random.nextInt(4);
                            // Schedule particle spawning
                            if (delay == 2) {
                                _level.sendParticles(
                                        ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_BIG.get(),
                                        this.getX() + xOffset,
                                        (this.getY() + 0.3) + yOffset,
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
            }
            if (tickCounter % 8 == 0) {
                if (this.level() instanceof ServerLevel _level) {
                    _level.sendParticles(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_AIR_BIG.get(), this.getX(), this.getY() + 0.2, this.getZ(), 1, 0, 0, 0., 0);
                }
            }
        }
    }

    public void hurtInRadius(Level level, double x, double y, double z, double radius, float damage) {
        // Create a Vec3 object for the center of the sphere
        Vec3 center = new Vec3(x, y, z);

        // Iterate through all living entities in the level
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, new net.minecraft.world.phys.AABB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius))) {
            // Check if the entity is a player
            if(entity.getType() != ModEntities.ELECTRO_FREDRICK.get() && entity.getType() != ModEntities.YELLOW_CHUCHU.get()) {
                // Calculate the distance between the player and the center point
                double distance = entity.position().distanceTo(center);


                // If the player is within the radius, apply damage
                if (distance <= radius) {
                    entity.hurt(entity.damageSources().mobAttack(this), damage);
                }
            }
                for (int i = 0; i <= 64; i++) {
                    if (i % 8 == 0) {
                        if (this.level() instanceof ServerLevel _level) {

                            double particleRadius = 1.5; // Radius of the sphere
                            int increment = 30; // Angle increment in degrees

                            for (int theta = 0; theta < 360; theta += increment) { // Horizontal angle (longitude)
                                for (int phi = 0; phi <= 180; phi += increment) { // Vertical angle (latitude)
                                    double radTheta = Math.toRadians(theta);
                                    double radPhi = Math.toRadians(phi);

                                    // Spherical to Cartesian conversion
                                    double xOffset = particleRadius * Math.sin(radPhi) * Math.cos(radTheta);
                                    double yOffset = particleRadius * Math.cos(radPhi);
                                    double zOffset = particleRadius * Math.sin(radPhi) * Math.sin(radTheta);

                                    // Calculate a random delay (up to 3 seconds)
                                    int delay = random.nextInt(4);
                                    int particleType = random.nextInt(4);
                                    // Schedule particle spawning
                                    if (delay == 2) {
                                        _level.sendParticles(
                                                ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_SMALL.get(),
                                                entity.getX() + xOffset,
                                                (entity.getY() + 0.3) + yOffset,
                                                entity.getZ() + zOffset,
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
                    }
                }
        }
    }
}
