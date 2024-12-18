package net.foxolli3.yahaha.worldgen;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_KOROK_SEED_BLOCKS_KEY = registerKey("korok_seed_infused_blocks");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest dirtReplacables = new BlockMatchTest(Blocks.DIRT);
        RuleTest gravelReplacables = new BlockMatchTest(Blocks.GRAVEL);

        List<OreConfiguration.TargetBlockState> overworld_korok_seed_blocks = List.of
                (OreConfiguration.target(dirtReplacables, ModBlocks.KOROK_SEED_INFUSED_DIRT.get().defaultBlockState()),
                OreConfiguration.target(gravelReplacables, ModBlocks.KOROK_SEED_INFUSED_GRAVEL.get().defaultBlockState()));


        register(context, OVERWORLD_KOROK_SEED_BLOCKS_KEY, Feature.ORE, new OreConfiguration(overworld_korok_seed_blocks,1));

    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, Feature<OreConfiguration> feature, OreConfiguration configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}