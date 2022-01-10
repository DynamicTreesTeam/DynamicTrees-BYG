package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RedwoodLogic extends VariateHeightLogic {

    public static final ConfigurationProperty<Integer> HEIGHT_OF_CANOPY = ConfigurationProperty.integer("height_of_canopy");

    public RedwoodLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(HEIGHT_OF_CANOPY, 8)
                .with(HEIGHT_VARIATION, 18)
                .with(LOWEST_BRANCH_VARIATION, 5);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final Species species = context.species();
        final World world = context.world();
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        final int radius = context.radius();
        final BlockPos pos = context.pos();
        Direction originDir = signal.dir.getOpposite();
        int treeHash = CoordUtils.coordHashCode(signal.rootPos, 2);

        //Alter probability map for direction change
        probMap[0] = 0;//Down is always disallowed for spruce
        probMap[1] = signal.isInTrunk() ? species.getUpProbability(): 0;

        boolean branchOut = (signal.numSteps + treeHash) % 5 == 0;
        int sideTurn = !signal.isInTrunk() || (signal.isInTrunk() && branchOut && radius > 1) ? 2 : 0;//Only allow turns when we aren't in the trunk(or the branch is not a twig)

        int canopyHeight = species.getLowestBranchHeight() + treeHash % 8 + configuration.get(HEIGHT_OF_CANOPY);

        if (signal.delta.getY() < canopyHeight) {
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 0;
            probMap[2 + getHashedVariation(world, pos, 4)] = sideTurn;
        } else {
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = //Only allow turns when we aren't in the trunk(or the branch is not a twig and step is odd)
                    !signal.isInTrunk() || (signal.isInTrunk() && signal.numSteps % 4 == 1 && radius > 1) ? 2 : 0;
        }


        probMap[originDir.ordinal()] = 0;//Disable the direction we came from
        probMap[signal.dir.ordinal()] += signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1;//Favor current travel direction

        if(!signal.isInTrunk() && signal.numTurns == 1 && signal.delta.distSqr(0, signal.delta.getY(), 0, false) <= 1.5 ) {
            //disable left and right if we JUST turned out of the trunk, this is to prevent branches from interfering with the other sides
            probMap[signal.dir.getClockWise().ordinal()] = probMap[signal.dir.getCounterClockWise().ordinal()] = 0;
        }

        return probMap;
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final Species species = context.species();
        final GrowSignal signal = context.signal();
        Direction newDir = super.selectNewDirection(configuration, context);
        int signalY = signal.delta.getY();
        int treeHash = CoordUtils.coordHashCode(signal.rootPos, 2);
        int canopyHeight = species.getLowestBranchHeight() + treeHash % 8 + configuration.get(HEIGHT_OF_CANOPY);
        float bottomSlope = 1 + (treeHash%10)/(float)0xFFF;

        if (signal.isInTrunk() && newDir != Direction.UP) { // Turned out of trunk
            if (signalY < canopyHeight)
                signal.energy = 3;
            else {
                signal.energy += 2;
                signal.energy /= 3.2f;
                float maxEnergy = Math.max(2, Math.min(8.2f, (signalY-canopyHeight)*bottomSlope ));
                signal.energy = Math.min(maxEnergy, signal.energy);
            }
        }

        return newDir;
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return super.getLowestBranchHeight(configuration, context) +
                (int)( getHashedVariation(context.world(),context.pos(),configuration.get(LOWEST_BRANCH_VARIATION)) * 0.5 );
    }

}
