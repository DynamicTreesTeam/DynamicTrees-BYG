package maxhyper.dtbyg.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class DTBYGClient {

    public static void setup() {
        RenderTypeLookup.setRenderLayer(DTBYGRegistries.ARISIAN_BLOOM_BRANCH, RenderType.cutoutMipped());
    }

}
