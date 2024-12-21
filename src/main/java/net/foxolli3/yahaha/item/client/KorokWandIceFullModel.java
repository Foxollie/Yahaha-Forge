package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandFull;
import net.foxolli3.yahaha.item.custom.KorokWandIceFull;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandIceFullModel extends GeoModel<KorokWandIceFull> {
    @Override
    public ResourceLocation getModelResource(KorokWandIceFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_full_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandIceFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_ice_full_trans.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandIceFull animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_full.animation.json");
    }
}
