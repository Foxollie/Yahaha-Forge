package net.foxolli3.yahaha.events;


import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.entity.custom.*;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Yahaha.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class YahahaEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.FREDRICK.get(), FredrickMob.setAttributes());
        event.put(ModEntities.CRYO_FREDRICK.get(), CryoFredrickMob.setAttributes());
        event.put(ModEntities.PYRO_FREDRICK.get(), PyroFredrickMob.setAttributes());
        event.put(ModEntities.ELECTRO_FREDRICK.get(), ElectroFredrickMob.setAttributes());
        event.put(ModEntities.HYDRO_FREDRICK.get(), HydroFredrickMob.setAttributes());
        event.put(ModEntities.BLUE_CHUCHU.get(), BlueChuchuMob.setAttributes());
        event.put(ModEntities.YELLOW_CHUCHU.get(), YellowChuchuMob.setAttributes());
    }
    @SubscribeEvent
    public static void entitySpawnRestriction(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.BLUE_CHUCHU.get(), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
