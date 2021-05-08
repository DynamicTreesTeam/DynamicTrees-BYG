package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public class BYGTintedRootyBlock extends RootyBlock {

    /**
     * Grassy blocks in BYG use a tint index of 1 instead of 0, so the tint indices for
     * the rooty soil block need to be shifted. Blocks using this class should use the model
     * roots_tint_2.json instead of roots.json
     */
    public BYGTintedRootyBlock(Block primitiveDirt) {
        super(primitiveDirt);
    }

    @Override
    public int colorMultiplier (BlockColors blockColors, BlockState state, @Nullable IBlockDisplayReader world, @Nullable BlockPos pos, int tintIndex){
        final int white = 0xFFFFFFFF;
        switch(tintIndex) {
            case 1: return blockColors.getColor(getPrimitiveDirt().defaultBlockState(), world, pos, tintIndex);
            case 2: return state.getBlock() instanceof RootyBlock ? rootColor(state, world, pos) : white;
            default: return white;
        }
    }

}
