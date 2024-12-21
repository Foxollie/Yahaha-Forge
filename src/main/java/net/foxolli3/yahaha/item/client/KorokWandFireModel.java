package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.item.custom.KorokWandBlock;
import net.foxolli3.yahaha.item.custom.KorokWandFire;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KorokWandFireModel extends GeoModel<KorokWandFire> {
    @Override
    public ResourceLocation getModelResource(KorokWandFire animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/korok_wand_block_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KorokWandFire animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/item/korok_wand_fire_clear.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KorokWandFire animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/korok_wand_block.animation.json");
    }
}
