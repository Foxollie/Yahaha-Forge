package net.foxolli3.yahaha.entity.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.ElectroFredrickMob;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElectroFredrickModel extends GeoModel<ElectroFredrickMob> {
    @Override
    public ResourceLocation getModelResource(ElectroFredrickMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/fredrick.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElectroFredrickMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/entity/electro_fredrick_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElectroFredrickMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/fredrick.animation.json");
    }

   /* @Override
    public void setCustomAnimations(FredrickMob animatable, long instanceId, AnimationState<FredrickMob> animationState) {
        CoreGeoBone korok = getAnimationProcessor().getBone("korok");

         if (korok != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            korok.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            korok.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    } */
}
