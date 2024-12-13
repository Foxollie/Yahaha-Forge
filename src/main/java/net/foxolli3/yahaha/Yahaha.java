package net.foxolli3.yahaha;

import com.mojang.logging.LogUtils;
import net.foxolli3.yahaha.block.ModBlocks;
import net.foxolli3.yahaha.block.entity.ModBlockEntities;
import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.entity.client.FredrickRenderer;
import net.foxolli3.yahaha.item.CreativeModTabs;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.loot.ModLootModifier;
import net.foxolli3.yahaha.screen.ModMenuTypes;
import net.foxolli3.yahaha.screen.WandConstructionTableScreen;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;

import javax.swing.text.html.parser.Entity;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Yahaha.MOD_ID)
public class Yahaha {
    public static final String MOD_ID = "yahaha";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Yahaha() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Moditems.register(modEventBus);
        ModBlocks.register(modEventBus);
        CreativeModTabs.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);
        ModLootModifier.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(Moditems.KOROK_SEED);
            event.accept(ModBlocks.KOROK_SEED_INFUSED_DIRT);
            event.accept(ModBlocks.KOROK_SEED_INFUSED_GRAVEL);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {


            MenuScreens.register(ModMenuTypes.WAND_CONSTRUCTION_TABLE_MENU.get(), WandConstructionTableScreen::new);

            EntityRenderers.register(ModEntities.FREDRICK.get(), FredrickRenderer::new);

        }
    }
    private void setup(final FMLCommonSetupEvent event) {

    }
}