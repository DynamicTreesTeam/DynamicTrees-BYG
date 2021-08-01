package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MegaPineLogic extends ConiferLogic {

    public MegaPineLogic(ResourceLocation registryName) {
        super(registryName, 3.5f);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {

        Direction originDir = signal.dir.getOpposite();
        int absDistance = Math.max(Math.abs(signal.delta.getX()), Math.abs(signal.delta.getZ()));

        if (signal.isInTrunk()){
            probMap[0] = 0;
            probMap[1] = species.getUpProbability();
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = signal.numSteps % 3 == 1 && radius > 1 ? 1 : 0;
        } else {
            if (absDistance == 1){
                int[] prob = new int[]{0,0,0,0,0,0};
                prob[signal.dir.ordinal()] = 1;
                return prob;
            }
            boolean isBranchAbove = TreeHelper.isBranch(world.getBlockState(pos.above()))
                    || Math.abs(CoordUtils.coordHashCode(pos, 2)) % 4 == 0;
            probMap[1] = 0;
            probMap[0] = !isBranchAbove ? 1 : 0;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = isBranchAbove ? 1 : 0;
        }

        probMap[originDir.ordinal()] = 0;//Disable the direction we came from

        return probMap;
    }

}
