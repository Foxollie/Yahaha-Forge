package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandBlock;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandBlockModel extends GeoModel<KorokWandBlock> {
    @Override
    public ResourceLocation getModelResource(KorokWandBlock animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandBlock animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandBlock animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_block.animation.json");
    }
}
