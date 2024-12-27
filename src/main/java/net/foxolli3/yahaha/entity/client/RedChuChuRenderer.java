package net.foxolli3.yahaha.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.RedChuchuMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RedChuChuRenderer extends GeoEntityRenderer<RedChuchuMob> {
    public RedChuChuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RedChuchuModel());
    }
    @Override
    public ResourceLocation getTextureLocation(RedChuchuMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/entity/red_chuchu_center.png");
    }
    @Override
    public void render(RedChuchuMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    @Override
    public @Nullable RenderType getRenderType(RedChuchuMob animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if(!animatable.isInvisible()) {
            return RenderType.entityTranslucent(this.getTextureLocation(animatable));
        } else{
            return super.getRenderType(animatable, texture, bufferSource, partialTick);
        }
    }
}
