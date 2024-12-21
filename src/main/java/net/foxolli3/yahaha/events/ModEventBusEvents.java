package net.foxolli3.yahaha.events;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.particle.ModParticles;
import net.foxolli3.yahaha.particle.custom.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yahaha.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RED_CHUCHU_BURST_PARTICLES.get(), RedChuchuParticles.Provider::new);
        event.registerSpriteSet(ModParticles.RED_CHUCHU_BURST_PARTICLES_SMALL.get(), RedChuchuParticlesSmall.Provider::new);
        event.registerSpriteSet(ModParticles.WHITE_CHUCHU_BURST_PARTICLES.get(), WhiteChuchuParticles.Provider::new);
        event.registerSpriteSet(ModParticles.WHITE_CHUCHU_BURST_PARTICLES_SMALL.get(), WhiteChuchuParticlesSmall.Provider::new);
        event.registerSpriteSet(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES.get(), YellowChuchuParticles.Provider::new);
        event.registerSpriteSet(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_SMALL.get(), YellowChuchuParticlesSmall.Provider::new);
        event.registerSpriteSet(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_SMALL_DOWN.get(), YellowChuchuParticlesSmall.Provider::new);
        event.registerSpriteSet(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_SMALL_RIGHT.get(), YellowChuchuParticlesSmall.Provider::new);
        event.registerSpriteSet(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_SMALL_LEFT.get(), YellowChuchuParticlesSmall.Provider::new);
        event.registerSpriteSet(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_BIG.get(), YellowChuchuParticlesBig.Provider::new);
        event.registerSpriteSet(ModParticles.YELLOW_CHUCHU_BURST_PARTICLES_AIR_BIG.get(), YellowChuchuParticlesAirBig.Provider::new);
    }
}
