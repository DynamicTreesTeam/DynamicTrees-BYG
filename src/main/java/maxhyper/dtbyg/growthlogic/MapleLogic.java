package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MapleLogic extends GrowthLogicKit {
    public MapleLogic(ResourceLocation registryName) {
        super(registryName);
    }

    private static final int canopyDepth = 3;
    private static final int branchingHeight = 3;

    private static final int zigzagUpChance = 5;

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();
        int lowestBranch = species.getLowestBranchHeight(world, signal.rootPos);
        int deltaYFromLowest = signal.delta.getY() - lowestBranch;
        // disable down direction
        probMap[0] = 0;
        if (!signal.isInTrunk() && deltaYFromLowest >= 0 && deltaYFromLowest <= branchingHeight) {
            boolean evenEnergy = signal.energy % 2 == 0 || CoordUtils.coordHashCode(pos, 2) % zigzagUpChance == 0;
            probMap[1] = evenEnergy ? 1 : 0;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = evenEnergy ? 0 : 1;
        } else {
            // disable up in the trunk if the signal is above the branching height, to force branching
            probMap[1] = (signal.isInTrunk() &&
                    deltaYFromLowest >= 0) ||
                    (deltaYFromLowest >= branchingHeight + canopyDepth) ?
                    0 : species.getUpProbability();
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 3;
            // If we're not in the trunk, have a small chance of growing up and never grow down
            if (!signal.isInTrunk() && signal.dir != Direction.UP) {
                // Reinforce current growth direction
                // Makes branches more straight to start out with, and then twistier
                int increase = signal.numTurns > 2 ? 0 : 2;
                probMap[signal.dir.ordinal()] += increase;
            }
        }

        // Disable the direction we came from
        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

    private float getHashedVariation (World world, BlockPos pos){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % 4);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public float getEnergy(World world, BlockPos pos, Species species, float signalEnergy) {
        return signalEnergy + getHashedVariation(world, pos);
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return (int) (lowestBranchHeight + getHashedVariation(world, pos));
    }
}
