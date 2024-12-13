package net.foxolli3.yahaha.block.entity;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Yahaha.MOD_ID);
    public static final RegistryObject<BlockEntityType<WandConstructionTableBlockEntity>> WAND_TABLE_BE =
            BLOCK_ENTITIES.register("wand_table_be", () ->
                    BlockEntityType.Builder.of(WandConstructionTableBlockEntity::new,
                            ModBlocks.WAND_CONSTRUCTION_TABLE.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
