package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandBlock;
import net.foxolli3.yahaha.item.custom.KorokWandPuffshroom;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandPuffshroomModel extends GeoModel<KorokWandPuffshroom> {
    @Override
    public ResourceLocation getModelResource(KorokWandPuffshroom animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_puffshroom.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandPuffshroom animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_puffshroom.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandPuffshroom animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_puffshroom.animation.json");
    }
}
