package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MapleLogic extends GrowthLogicKit {
    public MapleLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        // disable down direction
        probMap[0] = 0;
        // disable up in the trunk if the signal is above the branching height, to force branching
        probMap[1] = signal.isInTrunk() &&
                signal.energy > species.getLowestBranchHeight(world, signal.rootPos) ?
                0 : species.getUpProbability();
        // If we're in the trunk, have a higher chance of branching out. If not, then lower chance
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = signal.isInTrunk() ? 3 : 1;

        // If we're not in the trunk, have a small chance of growing up and never grow down
        if (!signal.isInTrunk()) {
            // Reinforce current growth direction
            // Makes branches more straight to start out with, and then twistier
            int increase = signal.numTurns > 2 ? 0 : 2;
            probMap[signal.dir.ordinal()] += increase;
        }

        // Disable the direction we came from
        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

    @Override
    public Direction newDirectionSelected(Species species, Direction newDir, GrowSignal signal) {
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
