package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PineLogic extends ConiferLogic {

    public PineLogic(ResourceLocation registryName) {
        super(registryName, 10);
        setHorizontalLimiter(2);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        //Alter probability map for direction change
        probMap[0] = 0;//Down is always disallowed for spruce
        probMap[1] = signal.isInTrunk() ? species.getUpProbability(): 0;
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = //Only allow turns when we aren't in the trunk(or the branch is not a twig and step is odd)
                !signal.isInTrunk() || (signal.isInTrunk() && radius > 1) ? 2 : 0;

        if (signal.isInTrunk())
            for (Direction dir : CoordUtils.HORIZONTALS)
                if (TreeHelper.isBranch(world.getBlockState(pos.offset(dir.getNormal())))){
                    probMap = new int[]{probMap[0],probMap[1],0,0,0,0};
                    probMap[dir.ordinal()] = 2;
                    break;
                }

        probMap[signal.dir.getOpposite().ordinal()] = 0;//Disable the direction we came from
        probMap[signal.dir.ordinal()] += signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1;//Favor current travel direction

        return probMap;
    }
}
