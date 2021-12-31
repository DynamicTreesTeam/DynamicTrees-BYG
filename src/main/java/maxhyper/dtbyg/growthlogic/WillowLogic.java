package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WillowLogic extends VariateHeightLogic {

    public WillowLogic(ResourceLocation registryName) {
        super(registryName);
        setHeightVariation(8);
    }

    private static final int canopyDepth = 4;
    private static final int canopyDepthMega = 6;

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        final Direction originDir = signal.dir.getOpposite();

        probMap[Direction.DOWN.ordinal()] = 2;

        int lowestBranch = species.getLowestBranchHeight(world, signal.rootPos);
        if (signal.delta.getY() >= lowestBranch + (species.isMegaSpecies()?canopyDepthMega:canopyDepth))
            probMap[Direction.UP.ordinal()] = 0;

        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

}
