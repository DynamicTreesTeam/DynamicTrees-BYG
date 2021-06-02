package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MegaPrairieOakLogic extends PrairieOakLogic {

    public MegaPrairieOakLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        // Can't grow down, set it's chance to 0
        probMap[0] = 0;

        // If we're in the trunk, have a higher chance of branching out. If not, then lower chance
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = signal.isInTrunk() ? 7 : 4;

        // Disable the direction we came from
        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

    @Override
    public Direction newDirectionSelected(Species species, Direction newDir, GrowSignal signal) {
        if (!signal.isInTrunk()) {
            if (signal.energy >= 3.5f) {
                // Increase energy when not in the trunk, to encourage branching out.
                signal.energy *= 2.25;
            }
        }

        return newDir;
    }
}
