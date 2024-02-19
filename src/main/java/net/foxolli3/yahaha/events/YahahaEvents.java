package net.foxolli3.yahaha.events;


import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.entity.custom.FredrickMob;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yahaha.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YahahaEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.FREDRICK.get(), FredrickMob.setAttributes());
    }

}
