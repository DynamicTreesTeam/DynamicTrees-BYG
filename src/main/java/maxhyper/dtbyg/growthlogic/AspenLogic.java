package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AspenLogic extends GrowthLogicKit {

    public AspenLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        // Alter probability map for direction change
        probMap[0] = signal.isInTrunk() ? 0 : 1;
        probMap[1] = signal.isInTrunk() ? 4 : 1;

        // Set all side probabilities to 0 to start out
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = 0;

        // Allow for turning if in trunk, otherwise reinforce current travel direction
        if (signal.isInTrunk()) {
            // Make it so every 2 blocks the trunk has a higher chance of branching out

            // Choose a random direction to do the branch
            int directionSelection = Math.abs(CoordUtils.coordHashCode(pos, 2)) % 4;

            probMap[2 + directionSelection] = (signal.numSteps % 2 == 0) ? 3 : 0;
        } else {
            // If we're twig or small branch, don't grow up or down
            if (radius < 3) {
                probMap[0] = 0;
                probMap[1] = 0;
            }

            // Reinforce current travel direction
            probMap[signal.dir.ordinal()] += (signal.numTurns == 1 ? 2 : 1);
        }

        // Disable the direction we came from
        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

    @Override
    public Direction newDirectionSelected(Species species, Direction newDir, GrowSignal signal) {
        // Turned out of trunk
        if (signal.isInTrunk() && newDir != Direction.UP) {
            // Reduce the energy so branches don't expand too much

            // Branches can have 1-3 energy
            float energyAddition = ((CoordUtils.coordHashCode(signal.rootPos.offset(signal.delta), 1) % 1000) / 1000.f) * 2.f;

            signal.energy = 1 + energyAddition;
        }
        return newDir;
    }

    private float getHashedVariation (World world, BlockPos pos){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % 3);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public float getEnergy(World world, BlockPos pos, Species species, float signalEnergy) {
        return signalEnergy + getHashedVariation(world, pos); // Vary the height energy by a psuedorandom hash function
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return (int) (lowestBranchHeight + getHashedVariation(world, pos));
    }
}
