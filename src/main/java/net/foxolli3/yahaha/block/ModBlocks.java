package net.foxolli3.yahaha.block;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.block.custom.WandConstructionTable;
import net.foxolli3.yahaha.item.Moditems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Yahaha.MOD_ID);
    public static final RegistryObject<Block> KOROK_SEED_INFUSED_DIRT = registerBlock("korok_seed_infused_dirt",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));
    public static final RegistryObject<Block> KOROK_SEED_INFUSED_GRAVEL = registerBlock("korok_seed_infused_gravel",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final RegistryObject<Block> KOROK_SEED_BLOCK = registerBlock("korok_seed_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_COPPER).sound(SoundType.METAL)));
    public static final RegistryObject<Block> FIERY_KOROK_BLOCK = registerBlock("fiery_korok_block",
            () -> new SlimeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).sound(SoundType.HONEY_BLOCK).noOcclusion().lightLevel(p_50872_ -> 8)));
    public static final RegistryObject<Block> ICY_KOROK_BLOCK = registerBlock("icy_korok_block",
            () -> new SlimeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).sound(SoundType.HONEY_BLOCK).noOcclusion().lightLevel(p_50872_ -> 2)));
    public static final RegistryObject<Block> ELECTRIC_KOROK_BLOCK = registerBlock("electric_korok_block",
            () -> new SlimeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).sound(SoundType.HONEY_BLOCK).noOcclusion().lightLevel(p_50872_ -> 6)));
    public static final RegistryObject<Block> WATERY_KOROK_BLOCK = registerBlock("watery_korok_block",
            () -> new SlimeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).sound(SoundType.HONEY_BLOCK).noOcclusion().lightLevel(p_50872_ -> 4)));

    public static final RegistryObject<Block> KOROK_SEED_TILES = registerBlock("korok_seed_tiles",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_COPPER).sound(SoundType.METAL)));

    public static final RegistryObject<Block> KOROK_SEED_TILE_STAIRS = registerBlock("korok_seed_tile_stairs",
            () -> new StairBlock(ModBlocks.KOROK_SEED_TILES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_COPPER).sound(SoundType.METAL)));
    public static final RegistryObject<Block> KOROK_SEED_TILE_SLAB = registerBlock("korok_seed_tile_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CUT_COPPER).sound(SoundType.METAL)));

    public static final RegistryObject<Block> WAND_CONSTRUCTION_TABLE = registerBlock("wand_construction_table",
            () -> new WandConstructionTable(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_WOOD).noOcclusion()));
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return Moditems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
