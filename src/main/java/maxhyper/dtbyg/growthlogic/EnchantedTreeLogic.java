package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.resources.ResourceLocation;

public class EnchantedTreeLogic extends TwistingTreeLogic {

    public static final ConfigurationProperty<Integer> FORCE_BRANCHING_AT_STEP = ConfigurationProperty.integer("force_branching_at_step");
    public static final ConfigurationProperty<Integer> FORCE_BRANCHING_AT_STEP_VARIATION = ConfigurationProperty.integer("force_branching_at_step_variation");

    public EnchantedTreeLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(FORCE_BRANCHING_AT_STEP, 10)
                .with(FORCE_BRANCHING_AT_STEP_VARIATION, 1);
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        this.register(FORCE_BRANCHING_AT_STEP, FORCE_BRANCHING_AT_STEP_VARIATION);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();

        int branchOutStep = configuration.get(FORCE_BRANCHING_AT_STEP) + VariateHeightLogic.getHashedVariation(context.level(), context.signal().rootPos, configuration.get(HEIGHT_VARIATION));
        int branchOutVar = configuration.get(FORCE_BRANCHING_AT_STEP_VARIATION);
        if (signal.numSteps >= branchOutStep && signal.numSteps <= branchOutStep + branchOutVar){
            probMap[0] = configuration.get(DOWN_PROBABILITY);
            probMap[1] = signal.numSteps == branchOutStep ? 0 : context.species().getUpProbability();;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 1;

            probMap[signal.dir.getOpposite().ordinal()] = 0; // Disable the direction we came from

            return probMap;
        }
        return super.populateDirectionProbabilityMap(configuration, context);
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return super.getLowestBranchHeight(configuration, context);
    }

}
