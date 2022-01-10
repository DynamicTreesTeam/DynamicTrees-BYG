package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.WaterSoilProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public class BYGTintedSoilProperties extends SoilProperties {

    public static final TypedRegistry.EntryType<SoilProperties> TYPE = TypedRegistry.newType(BYGTintedSoilProperties::new);

    public BYGTintedSoilProperties (final ResourceLocation registryName){
        super(null, registryName);
    }

    @Override
    protected RootyBlock createBlock(AbstractBlock.Properties blockProperties) {
        return new BYGTintedRootyBlock(this, blockProperties);
    }

    public static class BYGTintedRootyBlock extends RootyBlock {

        /**
         * Grassy blocks in BYG use a tint index of 1 instead of 0, so the tint indices for
         * the rooty soil block need to be shifted.
         */
        public BYGTintedRootyBlock(SoilProperties properties, Properties blockProperties) {
            super(properties, blockProperties);
        }

        @Override
        public int colorMultiplier (BlockColors blockColors, BlockState state, @Nullable IBlockDisplayReader world, @Nullable BlockPos pos, int tintIndex){
            final int white = 0xFFFFFFFF;
            switch(tintIndex) {
                case 1: return blockColors.getColor(getSoilProperties().getPrimitiveSoilBlock().defaultBlockState(), world, pos, tintIndex);
                case 2: return state.getBlock() instanceof RootyBlock ? rootColor(state, world, pos) : white;
                default: return white;
            }
        }

    }

}
