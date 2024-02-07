package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import potionstudios.byg.common.block.end.impariusgrove.TreeBranchBlock;

public class DynamicArisianBloomBranch extends TreeBranchBlock {

    private final Block baseBlock;
    public DynamicArisianBloomBranch(Block baseBlock) {
        super(BlockBehaviour.Properties.copy(baseBlock));
        this.baseBlock = baseBlock;
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos) || TreeHelper.isBranch(level.getBlockState(pos.relative(state.getValue(FACING).getOpposite())));
    }

    @Override
    public Item asItem() {
        return baseBlock.asItem();
    }

}
