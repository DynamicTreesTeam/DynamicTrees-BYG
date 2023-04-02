package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

public class BaobabLogic extends VariateHeightLogic {

    public BaobabLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(HEIGHT_VARIATION, 4);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final LevelAccessor world = context.level();
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        final BlockPos pos = context.pos();
        Direction originDir = signal.dir.getOpposite();

        if (!signal.isInTrunk()){
            Direction relativePosToRoot = Direction.fromNormal(signal.delta.getX(), 0, signal.delta.getY());
            if (relativePosToRoot != null){
                if (signal.energy > 2){ //Flaring at end points, higher min energy means more flaring
                    probMap[Direction.DOWN.ordinal()] = 0;
                    for (Direction dir: CoordUtils.HORIZONTALS){
                        probMap[dir.ordinal()] = 0;
                    }
                }
                boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot.getNormal())).getBlock() instanceof BranchBlock;
                boolean isBranchSide = world.getBlockState(pos.above()).getBlock() instanceof BranchBlock;
                probMap[Direction.UP.ordinal()] = isBranchUp && !isBranchSide? 0:2;
                probMap[relativePosToRoot.ordinal()] = isBranchSide && !isBranchUp? 0:3;
            }
        }

        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

}
