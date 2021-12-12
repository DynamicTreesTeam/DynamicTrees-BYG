package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EbonyLogic extends GrowthLogicKit {
    public EbonyLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        probMap[0] = species.getUpProbability();
        probMap[1] = signal.isInTrunk() &&
                signal.energy > species.getLowestBranchHeight(world, signal.rootPos) ?
                0 : species.getUpProbability();

        probMap[2] = probMap[3] = probMap[4] = probMap[5] = 4;

        if (!signal.isInTrunk())
            probMap[signal.dir.ordinal()] += 2;

        // Disable the direction we came from
        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

    private float getHashedVariation (World world, BlockPos pos){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % 10);//Vary the height energy by a psuedorandom hash function
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
