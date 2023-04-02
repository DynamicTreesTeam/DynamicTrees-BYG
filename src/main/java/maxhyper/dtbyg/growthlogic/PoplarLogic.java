package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class PoplarLogic extends VariateHeightLogic {

    public PoplarLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final int[] probMap = context.probMap();
        final GrowSignal signal = context.signal();

        Direction originDir = signal.dir.getOpposite();

        // Alter probability map for direction change
        probMap[0] = signal.isInTrunk() ? 0 : 1;
        probMap[1] = signal.isInTrunk() ? 4 : 1;
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = (((signal.isInTrunk() && signal.numSteps % 2 == 0) || !signal.isInTrunk()) && signal.energy < 8) ? 2 : 0;
        probMap[originDir.ordinal()] = 0; // Disable the direction we came from
        probMap[signal.dir.ordinal()] += (signal.isInTrunk() ? 0 : signal.numTurns == 1 ? 2 : 1); // Favor current travel direction

        return probMap;
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final GrowSignal signal = context.signal();
        final Direction newDir = super.selectNewDirection(configuration, context);

        if (signal.isInTrunk() && newDir != Direction.UP) { // Turned out of trunk
            if (signal.energy >= 4f) {
                signal.energy = 1.8f; // don't grow branches more than 1 block out from the trunk
            } else if (signal.energy < 5) {
                signal.energy = 0.8f; // don't grow branches, only leaves
            } else {
                signal.energy = 0; // don't grow branches or leaves
            }
        }

        return newDir;
    }

}
