package net.foxolli3.yahaha.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.custom.BlueChuchuMob;
import net.foxolli3.yahaha.item.custom.KorokWandFire;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BlueChuChuRenderer extends GeoEntityRenderer<BlueChuchuMob> {
    public BlueChuChuRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BlueChuchuModel());
    }
    @Override
    public ResourceLocation getTextureLocation(BlueChuchuMob animatable) {
        return ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/entity/blue_chuchu_center.png");
    }
    @Override
    public void render(BlueChuchuMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    @Override
    public @Nullable RenderType getRenderType(BlueChuchuMob animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if(!animatable.isInvisible()) {
            return RenderType.entityTranslucent(this.getTextureLocation(animatable));
        } else{
            return super.getRenderType(animatable, texture, bufferSource, partialTick);
        }
    }
}
