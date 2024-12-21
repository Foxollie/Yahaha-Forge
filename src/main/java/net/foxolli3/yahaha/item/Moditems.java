package net.foxolli3.yahaha.item;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.item.custom.*;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
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
    public static final RegistryObject<Item> PUFFSHROOM = ITEMS.register("puffshroom",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLUE_CHUCHU_JELLY = ITEMS.register("blue_chuchu_jelly",
            () -> new BlueChuchuJellyItem(new Item.Properties()));
    public static final RegistryObject<Item> RED_CHUCHU_JELLY = ITEMS.register("red_chuchu_jelly",
            () -> new RedChuchuJellyItem(new Item.Properties()));
    public static final RegistryObject<Item> WHITE_CHUCHU_JELLY = ITEMS.register("white_chuchu_jelly",
            () -> new WhiteChuchuJellyItem(new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_CHUCHU_JELLY = ITEMS.register("yellow_chuchu_jelly",
            () -> new YellowChuchuJellyItem(new Item.Properties()));

    public static final RegistryObject<Item> PUFFSHROOM_SWORD = ITEMS.register("puffshroom_sword",
            () -> new PuffshroomItem(Tiers.WOOD, new Item.Properties()));
    public static final RegistryObject<Item> KOROK_WAND_EMPTY = ITEMS.register("korok_wand_empty",
            () -> new KorokWandEmpty(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_FULL = ITEMS.register("korok_wand_full",
            () -> new KorokWandFull(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_BLOCK = ITEMS.register("korok_wand_block",
            () -> new KorokWandBlock(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_FIRE = ITEMS.register("korok_wand_fire",
            () -> new KorokWandIce(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> KOROK_WAND_FIRE_FULL = ITEMS.register("korok_wand_fire_full",
            () -> new KorokWandFireFull(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_ICE = ITEMS.register("korok_wand_ice",
            () -> new KorokWandFire(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_ICE_FULL = ITEMS.register("korok_wand_ice_full",
            () -> new KorokWandIceFull(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_THUNDER = ITEMS.register("korok_wand_thunder",
            () -> new KorokWandThunder(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_THUNDER_FULL = ITEMS.register("korok_wand_thunder_full",
            () -> new KorokWandThunderFull(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_WATER = ITEMS.register("korok_wand_water",
            () -> new KorokWandWater(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_WATER_FULL = ITEMS.register("korok_wand_water_full",
            () -> new KorokWandWaterFull(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> KOROK_WAND_PUFFSHROOM = ITEMS.register("korok_wand_puffshroom",
            () -> new KorokWandPuffshroom(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> KOROK_WAND_FROND = ITEMS.register("korok_wand_frond",
            () -> new KorokWandFrond(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> FREDRICK_WAND_SCHEMATIC = ITEMS.register("fredrick_wand_schematic",
            () -> new KorokSeedItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FREDRICK_SPAWN_EGG = ITEMS.register("fredrick_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FREDRICK, 0x4d3d20,0x7E864D,
                    new Item.Properties()));
    public static final RegistryObject<Item> CRYO_FREDRICK_SPAWN_EGG = ITEMS.register("cryo_fredrick_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CRYO_FREDRICK, 0xd6f4f9,0x91b3f1,
                    new Item.Properties()));
    public static final RegistryObject<Item> PYRO_FREDRICK_SPAWN_EGG = ITEMS.register("pyro_fredrick_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.PYRO_FREDRICK, 0xf25309,0x871306,
                    new Item.Properties()));
    public static final RegistryObject<Item> ELECTRO_FREDRICK_SPAWN_EGG = ITEMS.register("electro_fredrick_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ELECTRO_FREDRICK, 0x91fc00,0xd9ff05,
                    new Item.Properties()));

    public static final RegistryObject<Item> HYDRO_FREDRICK_SPAWN_EGG = ITEMS.register("hydro_fredrick_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.HYDRO_FREDRICK, 0x079cc4,0x00f5f9,
                    new Item.Properties()));

    public static final RegistryObject<Item> BLUE_CHUCHU_SPAWN_EGG = ITEMS.register("blue_chuchu_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BLUE_CHUCHU, 0x16d4e1,0xfb9d02,
                    new Item.Properties()));
    public static final RegistryObject<Item> YELLOW_CHUCHU_SPAWN_EGG = ITEMS.register("yellow_chuchu_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.YELLOW_CHUCHU, 0xc4f705,0x61b705,
                    new Item.Properties()));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}