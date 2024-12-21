package net.foxolli3.yahaha.particle;

import net.foxolli3.yahaha.Yahaha;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Yahaha.MOD_ID);
    public static final RegistryObject<SimpleParticleType> RED_CHUCHU_BURST_PARTICLES =
            PARTICLE_TYPES.register("red_chuchu_burst_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RED_CHUCHU_BURST_PARTICLES_SMALL =
            PARTICLE_TYPES.register("red_chuchu_burst_particles_small", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WHITE_CHUCHU_BURST_PARTICLES =
            PARTICLE_TYPES.register("white_chuchu_burst_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WHITE_CHUCHU_BURST_PARTICLES_SMALL =
            PARTICLE_TYPES.register("white_chuchu_burst_particles_small", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_CHUCHU_BURST_PARTICLES =
            PARTICLE_TYPES.register("yellow_chuchu_burst_particles", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_CHUCHU_BURST_PARTICLES_SMALL =
            PARTICLE_TYPES.register("yellow_chuchu_burst_particles_small", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_CHUCHU_BURST_PARTICLES_SMALL_DOWN =
            PARTICLE_TYPES.register("yellow_chuchu_burst_particles_small_down", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_CHUCHU_BURST_PARTICLES_SMALL_RIGHT =
            PARTICLE_TYPES.register("yellow_chuchu_burst_particles_small_right", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_CHUCHU_BURST_PARTICLES_SMALL_LEFT =
            PARTICLE_TYPES.register("yellow_chuchu_burst_particles_small_left", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_CHUCHU_BURST_PARTICLES_BIG =
            PARTICLE_TYPES.register("yellow_chuchu_burst_particles_big", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_CHUCHU_BURST_PARTICLES_AIR_BIG =
            PARTICLE_TYPES.register("yellow_chuchu_burst_particles_air_big", () -> new SimpleParticleType(true));


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
