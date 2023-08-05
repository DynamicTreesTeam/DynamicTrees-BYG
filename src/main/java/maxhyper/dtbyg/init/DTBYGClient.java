package maxhyper.dtbyg.init;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class DTBYGClient {

    public static void setup() {
        for (Block block : new Block[]{DTBYGRegistries.ARISIAN_BLOOM_BRANCH.get(), DTBYGRegistries.EMBUR_GEL_BRANCH.get(),
                DTBYGRegistries.WITCH_HAZEL_BRANCH.get(), DTBYGRegistries.IMPARIUS_MUSHROOM_BRANCH.get()}){
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
        }
    }

}
