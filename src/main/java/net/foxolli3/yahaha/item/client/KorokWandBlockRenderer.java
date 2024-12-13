package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.item.custom.KorokWandBlock;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandBlockRenderer extends GeoItemRenderer<KorokWandBlock> {

    public KorokWandBlockRenderer() {
        super(new KorokWandBlockModel());
    }
}
