package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandFrond;
import net.foxolli3.yahaha.item.custom.KorokWandPuffshroom;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandFrondModel extends GeoModel<KorokWandFrond> {
    @Override
    public ResourceLocation getModelResource(KorokWandFrond animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_frond.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandFrond animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_frond.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandFrond animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_frond.animation.json");
    }
}
