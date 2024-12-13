package net.foxolli3.yahaha.item;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.item.custom.KorokSeedItem;
import net.foxolli3.yahaha.item.custom.KorokWandBlock;
import net.foxolli3.yahaha.item.custom.KorokWandEmpty;
import net.foxolli3.yahaha.item.custom.KorokWandFull;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class Moditems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Yahaha.MOD_ID);
    public static final RegistryObject<Item> KOROK_SEED = ITEMS.register("korok_seed",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KOROK_SEED_POWDER = ITEMS.register("korok_seed_powder",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KAZOO_VIEW_HIGHWAY_DISC = ITEMS.register("kazoo_view_highway_music_disc",
            () -> new Item(new Item.Properties().jukeboxPlayable(ModSounds.KAZOO_VIEW_HIGHWAY_KEY).stacksTo(1)));
    public static final RegistryObject<Item> PIRANHA_PLANTS_ON_PARADE_KAZOO_COVER_MUSIC_DISC = ITEMS.register("piranha_plants_on_parade_kazoo_cover_music_disc",
            () -> new Item(new Item.Properties().jukeboxPlayable(ModSounds.PIRANHA_PLANTS_ON_PARADE_KAZOO_COVER_KEY).stacksTo(1)));
    public static final RegistryObject<Item> SUIKA_GAME_THEME_KAZOO_COVER_MUSIC_DISC = ITEMS.register("suika_game_theme_kazoo_cover_music_disc",
            () -> new Item(new Item.Properties().jukeboxPlayable(ModSounds.SUIKA_GAME_THEME_KAZOO_COVER_KEY).stacksTo(1)));
    public static final RegistryObject<Item> KOROK_FROND = ITEMS.register("korok_frond",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OCTOROK_EYEBALL = ITEMS.register("octorok_eyeball",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> KOROK_WAND_EMPTY = ITEMS.register("korok_wand_empty",
            () -> new KorokWandEmpty(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_FULL = ITEMS.register("korok_wand_full",
            () -> new KorokWandFull(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_BLOCK = ITEMS.register("korok_wand_block",
            () -> new KorokWandBlock(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FREDRICK_WAND_SCHEMATIC = ITEMS.register("fredrick_wand_schematic",
            () -> new KorokSeedItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FREDRICK_SPAWN_EGG = ITEMS.register("fredrick_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FREDRICK, 0x4d3d20,0x7E864D,
                    new Item.Properties()));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}