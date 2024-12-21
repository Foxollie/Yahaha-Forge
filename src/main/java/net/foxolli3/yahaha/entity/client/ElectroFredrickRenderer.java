package net.foxolli3.yahaha.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.ElectroFredrickMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ElectroFredrickRenderer extends GeoEntityRenderer<ElectroFredrickMob> {
    public ElectroFredrickRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ElectroFredrickModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ElectroFredrickMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/entity/electro_fredrick_texture.png");
    }

    @Override
    public void render(ElectroFredrickMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.3f,0.3f,0.3f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
