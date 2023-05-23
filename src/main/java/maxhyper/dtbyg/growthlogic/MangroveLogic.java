package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class MangroveLogic extends GrowthLogicKit {

    public MangroveLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        GrowSignal signal = context.signal();
        Direction newDir = super.selectNewDirection(configuration, context);
        if (newDir != Direction.UP) {
            signal.energy += 0.75f;
        }
        if (newDir == Direction.UP && signal.dir != Direction.UP) {
            signal.energy += (Math.max(Math.abs(signal.delta.getX()), Math.abs(signal.delta.getZ())) - 2f) * 1.5f;
        }
        return newDir;
    }

    private float getHashedVariation (Level world, BlockPos pos, int mod){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % mod);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public float getEnergy(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return context.species().getSignalEnergy() + getLowestBranchHeight(configuration, context) * (1.5f + (getHashedVariation(context.level(), context.pos(), 10) / 20)); // Vary the energy between 1.5 and 2.0 times the minimum branch height
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return (int) (context.species().getLowestBranchHeight() + getHashedVariation(context.level(), context.pos(), 9)); // Vary the minimum branch height by a psuedorandom hash function
    }

}
