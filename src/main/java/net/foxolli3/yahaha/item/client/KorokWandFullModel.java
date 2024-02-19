package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandBlock;
import net.foxolli3.yahaha.item.custom.KorokWandFull;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandFullModel extends GeoModel<KorokWandFull> {
    @Override
    public ResourceLocation getModelResource(KorokWandFull animatable) {
        return new ResourceLocation(Yahaha.MOD_ID, "geo/korok_wand_full.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandFull animatable) {
        return new ResourceLocation(Yahaha.MOD_ID, "textures/item/korok_wand_full.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandFull animatable) {
        return new ResourceLocation(Yahaha.MOD_ID, "animations/korok_wand_full.animation.json");
    }
}
