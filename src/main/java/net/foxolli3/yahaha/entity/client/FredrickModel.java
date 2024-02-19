package net.foxolli3.yahaha.entity.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.FredrickMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FredrickModel extends GeoModel<FredrickMob> {
    @Override
    public ResourceLocation getModelResource(FredrickMob animatable) {
        return new ResourceLocation(Yahaha.MOD_ID, "geo/fredrick.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FredrickMob animatable) {
        return new ResourceLocation(Yahaha.MOD_ID, "textures/entity/fredrick_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FredrickMob animatable) {
        return new ResourceLocation(Yahaha.MOD_ID, "animations/fredrick.animation.json");
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
