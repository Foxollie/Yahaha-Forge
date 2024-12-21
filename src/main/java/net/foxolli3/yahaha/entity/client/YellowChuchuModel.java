package net.foxolli3.yahaha.entity.client;

import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.YellowChuchuMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class YellowChuchuModel extends GeoModel<YellowChuchuMob> {
    @Override
    public ResourceLocation getModelResource(YellowChuchuMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "geo/blue_chuchu_center.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(YellowChuchuMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/entity/yellow_chuchu_center.png");
    }

    @Override
    public ResourceLocation getAnimationResource(YellowChuchuMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "animations/blue_chuchu.animation.json");
    }

   @Override
    public void setCustomAnimations(YellowChuchuMob animatable, long instanceId, AnimationState<YellowChuchuMob> animationState) {
        GeoBone chuchu = getAnimationProcessor().getBone("Chuchu");

         if (chuchu != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            chuchu.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            chuchu.setRotY((entityData.headPitch()+90) * Mth.DEG_TO_RAD);
        }
    }
}