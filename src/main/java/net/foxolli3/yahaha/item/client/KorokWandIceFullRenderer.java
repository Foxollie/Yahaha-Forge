package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.item.custom.KorokWandFull;
import net.foxolli3.yahaha.item.custom.KorokWandIce;
import net.foxolli3.yahaha.item.custom.KorokWandIceFull;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandIceFullRenderer extends GeoItemRenderer<KorokWandIceFull> {

    public KorokWandIceFullRenderer() {
        super(new KorokWandIceFullModel());
    }

    @Override
    public @Nullable RenderType getRenderType(KorokWandIceFull animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.itemEntityTranslucentCull(this.getTextureLocation(animatable));
    }
}
