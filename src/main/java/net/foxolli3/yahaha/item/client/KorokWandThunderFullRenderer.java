package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.item.custom.KorokWandThunderFull;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandThunderFullRenderer extends GeoItemRenderer<KorokWandThunderFull> {

    public KorokWandThunderFullRenderer() {
        super(new KorokWandThunderFullModel());
    }

    @Override
    public @Nullable RenderType getRenderType(KorokWandThunderFull animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.itemEntityTranslucentCull(this.getTextureLocation(animatable));
    }
}
