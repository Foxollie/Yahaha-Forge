package net.foxolli3.yahaha.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.HydroFredrickMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HydroFredrickRenderer extends GeoEntityRenderer<HydroFredrickMob> {
    public HydroFredrickRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HydroFredrickModel());
    }

    @Override
    public ResourceLocation getTextureLocation(HydroFredrickMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/entity/hydro_fredrick_texture.png");
    }

    @Override
    public void render(HydroFredrickMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.3f,0.3f,0.3f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
