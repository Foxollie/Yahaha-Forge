package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandWaterFull;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandWaterFullModel extends GeoModel<KorokWandWaterFull> {
    @Override
    public ResourceLocation getModelResource(KorokWandWaterFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_full_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandWaterFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_water_full_trans.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandWaterFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_full.animation.json");
    }
}
