package net.foxolli3.yahaha.sound;

import net.foxolli3.yahaha.Yahaha;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Yahaha.MOD_ID);

    public static final RegistryObject<SoundEvent> KAZOO_VIEW_HIGHWAY = registerSoundEvents("kazoo_view_highway");

    public static final RegistryObject<SoundEvent> PIRANHA_PLANTS_ON_PARADE_KAZOO_COVER = registerSoundEvents("piranha_plants_on_parade_kazoo_cover");

    public static final RegistryObject<SoundEvent> SUIKA_GAME_THEME_KAZOO_COVER = registerSoundEvents("suika_game_theme_kazoo_cover");

    public static final RegistryObject<SoundEvent> HESTU_DANCE = registerSoundEvents("hestu_dance");
    public static final RegistryObject<SoundEvent> KOROK_POP = registerSoundEvents("korok_pop");

    public static final RegistryObject<SoundEvent> FREDRICK_SPAWN = registerSoundEvents("fredrick_spawn");
    public static final RegistryObject<SoundEvent> FREDRICK_HURT = registerSoundEvents("fredrick_hurt");

    public static final RegistryObject<SoundEvent> FREDRICK_DEATH = registerSoundEvents("fredrick_death");
    public static final RegistryObject<SoundEvent> KOROK_QUESTION_1 = registerSoundEvents("korok_question_1");
    public static final RegistryObject<SoundEvent> KOROK_QUESTION_2 = registerSoundEvents("korok_question_2");

    public static final RegistryObject<SoundEvent> YAHAHA = registerSoundEvents("yahaha");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Yahaha.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
