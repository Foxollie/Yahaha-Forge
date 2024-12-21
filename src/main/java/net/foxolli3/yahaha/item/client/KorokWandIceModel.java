package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandFire;
import net.foxolli3.yahaha.item.custom.KorokWandIce;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandIceModel extends GeoModel<KorokWandIce> {
    @Override
    public ResourceLocation getModelResource(KorokWandIce animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_block_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandIce animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_ice_trans.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandIce animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_block.animation.json");
    }
}
