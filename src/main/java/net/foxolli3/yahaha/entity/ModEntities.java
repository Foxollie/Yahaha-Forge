package net.foxolli3.yahaha.entity;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.BlueChuchuMob;
import net.foxolli3.yahaha.entity.custom.FredrickMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.event.GeoRenderEvent;

import javax.annotation.Nullable;

public class ModEntities<T extends Entity>{
    @Nullable
    public T create(Level pLevel) {
        return this.create(pLevel);
    }
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Yahaha.MOD_ID);

    public static final RegistryObject<EntityType<FredrickMob>> FREDRICK =
        ENTITY_TYPES.register("fredrick",() -> EntityType.Builder.of(FredrickMob::new, MobCategory.CREATURE)
                .sized(0.75f,0.75f)
                .build( ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "fredrick").toString()));

    public static final RegistryObject<EntityType<BlueChuchuMob>> BLUE_CHUCHU =
            ENTITY_TYPES.register("blue_chuchu",() -> EntityType.Builder.of(BlueChuchuMob::new, MobCategory.MONSTER)
                    .sized(0.75f,0.75f)
                    .build(ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "blue_chuchu").toString()));

    public static void register(IEventBus eventBus) {
    ENTITY_TYPES.register(eventBus);
    }

}
