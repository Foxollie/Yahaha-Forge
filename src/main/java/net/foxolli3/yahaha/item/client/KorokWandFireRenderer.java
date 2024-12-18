package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.item.custom.KorokWandBlock;
import net.foxolli3.yahaha.item.custom.KorokWandFire;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandFireRenderer extends GeoItemRenderer<KorokWandFire> {

    public KorokWandFireRenderer() {
        super(new KorokWandFireModel());
    }
}
