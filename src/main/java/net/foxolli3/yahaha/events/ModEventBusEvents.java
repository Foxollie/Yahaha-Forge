package net.foxolli3.yahaha.events;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.particle.ModParticles;
import net.foxolli3.yahaha.particle.custom.RedChuchuParticles;
import net.foxolli3.yahaha.particle.custom.RedChuchuParticlesSmall;
import net.foxolli3.yahaha.particle.custom.WhiteChuchuParticles;
import net.foxolli3.yahaha.particle.custom.WhiteChuchuParticlesSmall;
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
    }
}
