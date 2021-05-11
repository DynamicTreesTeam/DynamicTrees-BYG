package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DeciduousOakLogic extends GrowthLogicKit {
    public DeciduousOakLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        // Can't grow down, set it's chance to 0
        probMap[0] = 0;

        // Set all side probabilities to 0 to start out
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = 0;

        if (signal.isInTrunk()) {
            // If we're in the trunk, have a flat rate to branch out
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 2;
        } else {
            // If we're a branch, only grow in our specified direction
            probMap[1] = 0;
            probMap[signal.dir.ordinal()] = 2;
        }

        // Disable the direction we came from
        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

    @Override
    public Direction newDirectionSelected(Species species, Direction newDir, GrowSignal signal) {
        // Turned out of trunk
        if (signal.isInTrunk() && newDir != Direction.UP) {
            // Set to 1-3
            signal.energy = 1 + ((CoordUtils.coordHashCode(signal.rootPos.offset(signal.delta), 1) % 1000) / 1000.f) * 2;
        }

        return newDir;
    }

    @Override
    public float getEnergy(World world, BlockPos pos, Species species, float signalEnergy) {
        return signalEnergy;
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return lowestBranchHeight;
    }
}
