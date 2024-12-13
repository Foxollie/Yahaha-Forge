package net.foxolli3.yahaha.sound;

import net.foxolli3.yahaha.Yahaha;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Yahaha.MOD_ID);

    public static final RegistryObject<SoundEvent> KAZOO_VIEW_HIGHWAY = registerSoundEvent("kazoo_view_highway");
    public static final ResourceKey<JukeboxSong> KAZOO_VIEW_HIGHWAY_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "kazoo_view_highway"));

    public static final RegistryObject<SoundEvent> PIRANHA_PLANTS_ON_PARADE_KAZOO_COVER = registerSoundEvent("piranha_plants_on_parade_kazoo_cover");
    public static final ResourceKey<JukeboxSong> PIRANHA_PLANTS_ON_PARADE_KAZOO_COVER_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "piranha_plants_on_parade_kazoo_cover"));

    public static final RegistryObject<SoundEvent> SUIKA_GAME_THEME_KAZOO_COVER = registerSoundEvent("suika_game_theme_kazoo_cover");
    public static final ResourceKey<JukeboxSong> SUIKA_GAME_THEME_KAZOO_COVER_KEY = ResourceKey.create(Registries.JUKEBOX_SONG,
            ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "suika_game_theme_kazoo_cover"));

    public static final RegistryObject<SoundEvent> HESTU_DANCE = registerSoundEvent("hestu_dance");
    public static final RegistryObject<SoundEvent> KOROK_POP = registerSoundEvent("korok_pop");

    public static final RegistryObject<SoundEvent> FREDRICK_SPAWN = registerSoundEvent("fredrick_spawn");
    public static final RegistryObject<SoundEvent> FREDRICK_HURT = registerSoundEvent("fredrick_hurt");

    public static final RegistryObject<SoundEvent> FREDRICK_DEATH = registerSoundEvent("fredrick_death");
    public static final RegistryObject<SoundEvent> KOROK_QUESTION_1 = registerSoundEvent("korok_question_1");
    public static final RegistryObject<SoundEvent> KOROK_QUESTION_2 = registerSoundEvent("korok_question_2");
    public static final RegistryObject<SoundEvent> YAHAHA = registerSoundEvent("yahaha");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
