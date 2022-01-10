package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MangroveLogic extends GrowthLogicKit {

    public MangroveLogic(ResourceLocation registryName) {
        super(registryName);
    }

//    @Override
//    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
//
//        if (newDir != Direction.UP) {
//            signal.energy += 0.75f;
//        }
//        if (newDir == Direction.UP && signal.dir != Direction.UP) {
//            signal.energy += (Math.max(Math.abs(signal.delta.getX()), Math.abs(signal.delta.getZ())) - 2f) * 1.5f;
//        }
//        return newDir;
//    }
//
//    private float getHashedVariation (World world, BlockPos pos, int mod){
//        long day = world.getGameTime() / 24000L;
//        int month = (int)day / 30;//Change the hashs every in-game month
//        return (CoordUtils.coordHashCode(pos.above(month), 2) % mod);//Vary the height energy by a psuedorandom hash function
//    }
//
//    @Override
//    public float getEnergy(World world, BlockPos rootPos, Species species, float signalEnergy) {
//        return signalEnergy + getLowestBranchHeight(world, rootPos, species, species.getLowestBranchHeight()) * (1.5f + (getHashedVariation(world, rootPos, 10) / 20)); // Vary the energy between 1.5 and 2.0 times the minimum branch height
//
//    }
//
//    @Override
//    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
//        return (int) (lowestBranchHeight + getHashedVariation(world, pos, 9)); // Vary the minimum branch height by a psuedorandom hash function
//    }
}
