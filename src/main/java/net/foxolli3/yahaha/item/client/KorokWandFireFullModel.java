package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandFireFull;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandFireFullModel extends GeoModel<KorokWandFireFull> {
    @Override
    public ResourceLocation getModelResource(KorokWandFireFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_full_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandFireFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_fire_full_trans.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandFireFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_full.animation.json");
    }
}
