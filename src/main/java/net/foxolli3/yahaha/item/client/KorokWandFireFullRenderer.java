package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.item.custom.KorokWandFireFull;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandFireFullRenderer extends GeoItemRenderer<KorokWandFireFull> {

    public KorokWandFireFullRenderer() {
        super(new KorokWandFireFullModel());
    }

    @Override
    public @Nullable RenderType getRenderType(KorokWandFireFull animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.itemEntityTranslucentCull(this.getTextureLocation(animatable));
    }
}
