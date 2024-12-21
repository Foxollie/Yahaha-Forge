package net.foxolli3.yahaha.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.CryoFredrickMob;
import net.foxolli3.yahaha.entity.custom.PyroFredrickMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PyroFredrickRenderer extends GeoEntityRenderer<PyroFredrickMob> {
    public PyroFredrickRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PyroFredrickModel());
    }

    @Override
    public ResourceLocation getTextureLocation(PyroFredrickMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/entity/pyro_fredrick_texture.png");
    }

    @Override
    public void render(PyroFredrickMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.3f,0.3f,0.3f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
