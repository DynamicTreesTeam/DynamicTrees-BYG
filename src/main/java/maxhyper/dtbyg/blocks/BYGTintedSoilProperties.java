package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.rooty.RootyBlock;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BYGTintedSoilProperties extends SoilProperties {

    public static final TypedRegistry.EntryType<SoilProperties> TYPE = TypedRegistry.newType(BYGTintedSoilProperties::new);

    public BYGTintedSoilProperties (final ResourceLocation registryName){
        super(null, registryName);
    }

    @Override
    protected RootyBlock createBlock(Block.Properties blockProperties) {
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
        public int colorMultiplier(BlockColors blockColors, BlockState state, @org.jetbrains.annotations.Nullable BlockAndTintGetter level, @org.jetbrains.annotations.Nullable BlockPos pos, int tintIndex) {
            final int white = 0xFFFFFFFF;
            switch(tintIndex) {
                case 1: return blockColors.getColor(getSoilProperties().getPrimitiveSoilBlock().defaultBlockState(), level, pos, tintIndex);
                case 2: return state.getBlock() instanceof RootyBlock ? rootColor(state, level, pos) : white;
                default: return white;
            }
        }

    }

}
