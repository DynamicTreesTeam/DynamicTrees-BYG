package maxhyper.dtbyg.init;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class DTBYGClient {

    public static void setup() {
        for (Block block : new Block[]{DTBYGRegistries.ARISIAN_BLOOM_BRANCH.get(), DTBYGRegistries.EMBUR_GEL_BRANCH.get(),
                DTBYGRegistries.WITCH_HAZEL_BRANCH.get(), DTBYGRegistries.IMPARIUS_MUSHROOM_BRANCH.get(), DTBYGRegistries.FUNGAL_IMPARIUS_FILAMENT.get()}){
            ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
        }
        Block embur_sapling = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("dtbyg","embur_sapling"));
        if (embur_sapling != null)
            ItemBlockRenderTypes.setRenderLayer(embur_sapling, RenderType.translucent());
        Block embur_gel = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("dtbyg","embur_gel"));
        Block embur_gel_center = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("dtbyg","embur_gel_center"));
        if (embur_gel != null && embur_gel_center != null){
            ItemBlockRenderTypes.setRenderLayer(embur_gel, RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(embur_gel_center, RenderType.translucent());
        }
    }

}
