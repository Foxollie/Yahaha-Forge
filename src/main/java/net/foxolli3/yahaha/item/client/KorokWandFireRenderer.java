package net.foxolli3.yahaha.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxolli3.yahaha.item.custom.KorokWandFire;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandFireRenderer extends GeoItemRenderer<KorokWandFire> {

    public KorokWandFireRenderer(KorokWandFireModel korokWandFireModel) {
        super(new KorokWandFireModel());
        this.korokWandFireModel = korokWandFireModel;
    }

    private final KorokWandFireModel korokWandFireModel;

    @Override
    public @Nullable RenderType getRenderType(KorokWandFire animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.itemEntityTranslucentCull(this.getTextureLocation(animatable));
    }

    /*@Override
    public void preRender(PoseStack poseStack, KorokWandFire animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        VertexConsumer vb = bufferSource.getBuffer(RenderType.itemEntityTranslucentCull(this.getTextureLocation(animatable)));
        this.renderFinal(poseStack, animatable, model, bufferSource, vb, partialTick, packedLight, packedOverlay, colour);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }*/
}
