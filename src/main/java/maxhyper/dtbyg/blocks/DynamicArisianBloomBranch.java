package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import corgiaoc.byg.common.properties.blocks.end.impariusgrove.ImpariusMushroomBranchBlock;
import corgiaoc.byg.core.BYGBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class DynamicArisianBloomBranch extends ImpariusMushroomBranchBlock {

    public DynamicArisianBloomBranch(Properties builder) {
        super(builder);
    }

    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return super.canSurvive(state, worldIn, pos) || TreeHelper.isBranch(worldIn.getBlockState(pos.relative(state.getValue(FACING).getOpposite())));
    }

    @Override
    public Item asItem() {
        return BYGBlocks.ARISIAN_BLOOM_BRANCH.asItem();
    }

}
