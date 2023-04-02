package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class VariateHeightLogic extends GrowthLogicKit {

    public static final ConfigurationProperty<Integer> LOWEST_BRANCH_VARIATION = ConfigurationProperty.integer("lowest_branch_variation");

    public VariateHeightLogic(ResourceLocation registryName) { super(registryName); }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(HEIGHT_VARIATION, 6);
    }

    @Override
    protected void registerProperties() {
        this.register(HEIGHT_VARIATION);
    }

    public static int getHashedVariation (LevelAccessor world, BlockPos pos, int heightVariation){
        long day = world.dayTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % heightVariation);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public float getEnergy(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        Level world = context.level();
        BlockPos pos = context.pos();
        return super.getEnergy(configuration, context) * context.species().biomeSuitability(world, pos)
                + getHashedVariation(world, pos, configuration.get(HEIGHT_VARIATION));
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return super.getLowestBranchHeight(configuration, context)
                + getHashedVariation(context.level(), context.pos(), configuration.get(HEIGHT_VARIATION));
    }

}
