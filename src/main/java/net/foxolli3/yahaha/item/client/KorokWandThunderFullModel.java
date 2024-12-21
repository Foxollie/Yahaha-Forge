package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandThunderFull;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandThunderFullModel extends GeoModel<KorokWandThunderFull> {
    @Override
    public ResourceLocation getModelResource(KorokWandThunderFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_full_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandThunderFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_thunder_full_trans.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandThunderFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_full.animation.json");
    }
}
