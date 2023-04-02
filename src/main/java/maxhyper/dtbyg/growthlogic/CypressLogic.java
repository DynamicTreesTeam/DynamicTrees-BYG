package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;

public class CypressLogic extends VariateHeightLogic {

    public static final ConfigurationProperty<Float> CANOPY_ENERGY = ConfigurationProperty.floatProperty("canopy_energy");
    public static final ConfigurationProperty<Float> BOTTOM_BRANCH_SIDE_ENERGY = ConfigurationProperty.floatProperty("bottom_branch_side_energy");

    public CypressLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(CANOPY_ENERGY, 10.5f)
                .with(BOTTOM_BRANCH_SIDE_ENERGY, 4.5f)
                .with(HEIGHT_VARIATION, 10);
    }

    @Override
    protected void registerProperties() {
        this.register(CANOPY_ENERGY, BOTTOM_BRANCH_SIDE_ENERGY, HEIGHT_VARIATION);
    }


    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final int[] probMap = context.probMap();
        final GrowSignal signal = context.signal();
        Direction originDir = context.signal().dir.getOpposite();

        int treeHash = CoordUtils.coordHashCode(signal.rootPos, 2);

        probMap[0] = 0;
        probMap[1] = context.species().getUpProbability();
        probMap[2] = probMap[3] = probMap[4] = probMap[5] =  2;

        if (signal.energy > configuration.get(CANOPY_ENERGY)){

            probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;

            if (signal.delta.getY() <= configuration.getLowestBranchHeight(context) + 1) {
                probMap[1] += 3;
                int sideHash = treeHash % 16;
                probMap[2] = sideHash % 2 < 1 ? 1 : 0;
                probMap[3] = sideHash % 4 < 2 ? 1 : 0;
                probMap[4] = sideHash % 8 < 4 ? 1 : 0;
                probMap[5] = sideHash < 8 ? 1 : 0;
            }

        } else {

            //incentivize travelling up in trunk
            if (signal.isInTrunk())
                probMap[1] += 1;
            else if (context.signal().numTurns == 1 && context.signal().delta.distSqr(new Vec3i(0, context.signal().delta.getY(), 0)) <= 1.0)
                probMap[1] = 0;
            //Ensure that the branch gets out of the trunk at least two blocks so it won't interfere with the main trunk branch
        }

        probMap[originDir.ordinal()] = 0;

        return probMap;
    }


    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final Direction newDir = super.selectNewDirection(configuration, context);
        if (context.signal().isInTrunk() && newDir != Direction.UP && // Turned out of trunk.
                context.signal().delta.getY() <= configuration.getLowestBranchHeight(context) + 1) { // Lowest surface branches
            context.signal().energy = configuration.get(BOTTOM_BRANCH_SIDE_ENERGY);
        }
        return newDir;
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return context.species().getLowestBranchHeight();
    }
}
