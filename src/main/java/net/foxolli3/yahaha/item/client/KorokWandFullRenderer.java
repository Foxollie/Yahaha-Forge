package net.foxolli3.yahaha.item.client;

import net.foxolli3.yahaha.item.custom.KorokWandBlock;
import net.foxolli3.yahaha.item.custom.KorokWandFull;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class KorokWandFullRenderer extends GeoItemRenderer<KorokWandFull> {

    public KorokWandFullRenderer() {
        super(new KorokWandFullModel());
    }
}
