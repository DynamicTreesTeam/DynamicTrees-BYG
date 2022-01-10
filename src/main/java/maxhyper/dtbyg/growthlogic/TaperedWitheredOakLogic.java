package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TaperedWitheredOakLogic extends ConiferLogic {

    public TaperedWitheredOakLogic(ResourceLocation registryName) { super(registryName); }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(ENERGY_DIVISOR, 6F)
                .with(HORIZONTAL_LIMITER, 6F)
                .with(HEIGHT_VARIATION, 7);
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
        probMap[0] = 0;//Down is always disallowed for jungle
        probMap[1] = signal.isInTrunk() ? species.getUpProbability() : 1;
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = 0;

        int lowestBranch = species.getLowestBranchHeight();
        int height = lowestBranch*2 + ((treeHash % 7829) % 3);

        if (signal.delta.getY() >= height) {
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = //Only allow turns when we aren't in the trunk(or the branch is not a twig)
                    !signal.isInTrunk() || radius > 1 ? 2 : 0;
            probMap[originDir.ordinal()] = 0;//Disable the direction we came from
            probMap[signal.dir.ordinal()] += signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1;//Favor current travel direction
        } else if (signal.delta.getY() == lowestBranch) {
            if (!signal.isInTrunk()){
                probMap[1] = (signal.energy >= 2)? 0 : 1;
                probMap[2] = probMap[3] = probMap[4] = probMap[5] = (signal.energy >= 3)? 0 : 1;
                probMap[originDir.ordinal()] = 1;
            } else {
                int sideHash = treeHash % 16;
                probMap[2] = sideHash % 2 < 1 ? 1 : 0;
                probMap[3] = sideHash % 4 < 2 ? 1 : 0;
                probMap[4] = sideHash % 8 < 4 ? 1 : 0;
                probMap[5] = sideHash < 8 ? 1 : 0;
            }
        }

        probMap[originDir.ordinal()] = 0;//Disable the direction we came from
        probMap[signal.dir.ordinal()] += signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1;//Favor current travel direction

        return probMap;
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final Species species = context.species();
        final GrowSignal signal = context.signal();
        Direction newDir = super.selectNewDirection(configuration, context);

        if (signal.delta.getY() == species.getLowestBranchHeight() &&
                signal.isInTrunk() && newDir != Direction.UP) //Turned out of trunk
                signal.energy = 5;

        return newDir;
    }
}
