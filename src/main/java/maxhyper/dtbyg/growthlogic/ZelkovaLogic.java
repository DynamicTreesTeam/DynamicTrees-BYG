package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class ZelkovaLogic extends ConiferLogic {

    public ZelkovaLogic(ResourceLocation registryName) { super(registryName); }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(ENERGY_DIVISOR, 10F)
                .with(HORIZONTAL_LIMITER, 2F)
                .with(VariateHeightLogic.LOWEST_BRANCH_VARIATION, 7);
    }

    @Override
    protected void registerProperties() {
        this.register(ENERGY_DIVISOR, HORIZONTAL_LIMITER, HEIGHT_VARIATION, VariateHeightLogic.LOWEST_BRANCH_VARIATION);
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        Species species = context.species();
        BlockPos pos = context.pos();
        long day = context.world().getGameTime() / 24000L;
        int month = (int) day / 30;//Change the hashs every in-game month

        return (int)(super.getLowestBranchHeight(configuration, context) * species.biomeSuitability(context.world(), pos) +
                (CoordUtils.coordHashCode(pos.above(month), 2) % configuration.get(VariateHeightLogic.LOWEST_BRANCH_VARIATION)));//Vary the height energy by a psuedorandom hash function
    }

}
