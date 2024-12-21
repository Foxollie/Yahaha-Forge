package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandWater;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandWaterModel extends GeoModel<KorokWandWater> {
    @Override
    public ResourceLocation getModelResource(KorokWandWater animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_block_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandWater animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_water_trans.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandWater animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_block.animation.json");
    }
}
