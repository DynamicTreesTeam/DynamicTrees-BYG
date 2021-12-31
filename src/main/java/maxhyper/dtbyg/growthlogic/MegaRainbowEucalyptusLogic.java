package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MegaRainbowEucalyptusLogic extends GrowthLogicKit {
    public MegaRainbowEucalyptusLogic(ResourceLocation registryName) {
        super(registryName);
    }

    private static final float canopyHeightFraction = 0.5f;
    private static final float splitHeightFraction = 0.4f;

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        float energy = species.getEnergy(world, signal.rootPos);
        if ((signal.delta.getY() > energy * splitHeightFraction && signal.isInTrunk()) ||
                signal.delta.getY() > energy * canopyHeightFraction)
            probMap[Direction.UP.ordinal()] = 0;
        return probMap;
    }

}
