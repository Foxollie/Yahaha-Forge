package net.foxolli3.yahaha.item;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Yahaha.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register("yahaha_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Moditems.KOROK_SEED.get()))
                    .title(Component.translatable("creativetab.yahaha_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(Moditems.KOROK_SEED.get());
                        pOutput.accept(Moditems.KOROK_SEED_POWDER.get());
                        pOutput.accept(Moditems.KOROK_WAND_EMPTY.get());
                        pOutput.accept(Moditems.KOROK_FROND.get());
                        pOutput.accept(Moditems.OCTOROK_EYEBALL.get());
                        pOutput.accept(Moditems.KOROK_WAND_BLOCK.get());
                        pOutput.accept(Moditems.KOROK_WAND_FULL.get());
                        pOutput.accept(Moditems.KOROK_WAND_FIRE.get());
                        pOutput.accept(Moditems.KAZOO_VIEW_HIGHWAY_DISC.get());
                        pOutput.accept(Moditems.PIRANHA_PLANTS_ON_PARADE_KAZOO_COVER_MUSIC_DISC.get());
                        pOutput.accept(Moditems.SUIKA_GAME_THEME_KAZOO_COVER_MUSIC_DISC.get());
                        pOutput.accept(Moditems.FREDRICK_SPAWN_EGG.get());
                        pOutput.accept(Moditems.BLUE_CHUCHU_SPAWN_EGG.get());
                        pOutput.accept(Moditems.FREDRICK_WAND_SCHEMATIC.get());
                        pOutput.accept(Moditems.PUFFSHROOM.get());
                        pOutput.accept(Moditems.KOROK_WAND_PUFFSHROOM.get());
                        pOutput.accept(Moditems.KOROK_WAND_FROND.get());
                        pOutput.accept(Moditems.BLUE_CHUCHU_JELLY.get());
                        pOutput.accept(Moditems.RED_CHUCHU_JELLY.get());
                        pOutput.accept(Moditems.WHITE_CHUCHU_JELLY.get());
                        pOutput.accept(Moditems.YELLOW_CHUCHU_JELLY.get());


                        pOutput.accept(ModBlocks.KOROK_SEED_INFUSED_DIRT.get());
                        pOutput.accept(ModBlocks.KOROK_SEED_INFUSED_GRAVEL.get());
                        pOutput.accept(ModBlocks.KOROK_SEED_BLOCK.get());
                        pOutput.accept(ModBlocks.KOROK_SEED_TILES.get());
                        pOutput.accept(ModBlocks.KOROK_SEED_TILE_SLAB.get());
                        pOutput.accept(ModBlocks.KOROK_SEED_TILE_STAIRS.get());
                        pOutput.accept(ModBlocks.FIERY_KOROK_BLOCK.get());
                        pOutput.accept(ModBlocks.ICY_KOROK_BLOCK.get());
                        pOutput.accept(ModBlocks.ELECTRIC_KOROK_BLOCK.get());
                        pOutput.accept(ModBlocks.WATERY_KOROK_BLOCK.get());
                        pOutput.accept(ModBlocks.WAND_CONSTRUCTION_TABLE.get());

                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
