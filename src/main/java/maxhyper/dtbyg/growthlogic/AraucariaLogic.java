package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;

public class AraucariaLogic extends VariateHeightLogic {

    public static final ConfigurationProperty<Float> CANOPY_HEIGHT_FACTOR = ConfigurationProperty.floatProperty("canopy_height_factor");
    public static final ConfigurationProperty<Float> SPLIT_HEIGHT_FACTOR = ConfigurationProperty.floatProperty("split_height_factor");

    public AraucariaLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(CANOPY_HEIGHT_FACTOR, 0.8f)
                .with(SPLIT_HEIGHT_FACTOR, 0.6f)
                .with(HEIGHT_VARIATION, 13)
                .with(LOWEST_BRANCH_VARIATION, 7);
    }

    @Override
    protected void registerProperties() {
        this.register(CANOPY_HEIGHT_FACTOR, SPLIT_HEIGHT_FACTOR, HEIGHT_VARIATION, LOWEST_BRANCH_VARIATION);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();

        //Alter probability map for direction change
        float energy = context.species().getEnergy(context.level(), signal.rootPos);
        float splitCanopyHeight = energy * configuration.get(SPLIT_HEIGHT_FACTOR);
        if ((signal.delta.getY() > splitCanopyHeight && signal.isInTrunk()) ||
                signal.delta.getY() > energy * configuration.get(CANOPY_HEIGHT_FACTOR))
            probMap[Direction.UP.ordinal()] = 0;

        if (signal.delta.getY() < splitCanopyHeight){
            //Ensure that the branch doesn't go up after turning out of trunk so it won't interfere with it
            if (signal.numTurns == 1 && signal.delta.distSqr(new Vec3i(0, signal.delta.getY(), 0)) <= 1.0) {
                probMap[Direction.UP.ordinal()] = 0;
            }
            if (signal.isInTrunk()){
                probMap[Direction.UP.ordinal()] *= 10;
            }
        }


        return probMap;
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final GrowSignal signal = context.signal();
        Direction newDir = super.selectNewDirection(configuration, context);

        float energy = context.species().getEnergy(context.level(), signal.rootPos);
        // if signal just turned out of trunk under the split height
        if (signal.isInTrunk() && newDir != Direction.UP && signal.delta.getY() < energy * configuration.get(SPLIT_HEIGHT_FACTOR)) {
            signal.energy = Math.min(2.5f, signal.energy);
        }

        return newDir;
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return super.getLowestBranchHeight(configuration, context) + (int)getHashedVariation(context.level(), context.pos(), configuration.get(LOWEST_BRANCH_VARIATION));
    }
}
