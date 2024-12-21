package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.item.custom.KorokWandWaterFull;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandWaterFullRenderer extends GeoItemRenderer<KorokWandWaterFull> {

    public KorokWandWaterFullRenderer() {
        super(new KorokWandWaterFullModel());
    }

    @Override
    public @Nullable RenderType getRenderType(KorokWandWaterFull animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.itemEntityTranslucentCull(this.getTextureLocation(animatable));
    }
}
