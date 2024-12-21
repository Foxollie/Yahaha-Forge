package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.item.custom.KorokWandWater;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandWaterRenderer extends GeoItemRenderer<KorokWandWater> {

    public KorokWandWaterRenderer(KorokWandWaterModel korokWandWaterModel) {
        super(new KorokWandWaterModel());
        this.korokWandWaterModel = korokWandWaterModel;
    }

    private final KorokWandWaterModel korokWandWaterModel;

    @Override
    public @Nullable RenderType getRenderType(KorokWandWater animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.itemEntityTranslucentCull(this.getTextureLocation(animatable));
    }

    /*@Override
    public void preRender(PoseStack poseStack, KorokWandWater animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        VertexConsumer vb = bufferSource.getBuffer(RenderType.itemEntityTranslucentCull(this.getTextureLocation(animatable)));
        this.renderFinal(poseStack, animatable, model, bufferSource, vb, partialTick, packedLight, packedOverlay, colour);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }*/
}
