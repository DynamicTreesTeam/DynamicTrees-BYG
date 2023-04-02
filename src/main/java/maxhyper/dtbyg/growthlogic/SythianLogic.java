package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

public class SythianLogic extends GrowthLogicKit {

    public static final ConfigurationProperty<Integer> THICKEN_THRESHOLD = ConfigurationProperty.integer("thicken_threshold");

    public SythianLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(THICKEN_THRESHOLD, 5)
                .with(HEIGHT_VARIATION, 13);
    }

    @Override
    protected void registerProperties() {
        this.register(THICKEN_THRESHOLD, HEIGHT_VARIATION);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        Direction originDir = signal.dir.getOpposite();

        //Alter probability map for direction change
        probMap[0] = 0;//Down is always disallowed
        probMap[1] = signal.isInTrunk() ? context.species().getUpProbability() : 0;
        probMap[2] = probMap[3] = probMap[4] = probMap[5] = //Only allow turns when we aren't in the trunk(or the branch is not a twig and step is odd)
                !signal.isInTrunk() || (signal.isInTrunk() && signal.numSteps % 2 == 0) ? 1+(signal.isInTrunk() ? (int)(signal.delta.getY()/(configuration.getEnergy(context)/4)) : 0) : 0;
        probMap[originDir.ordinal()] = 0;//Disable the direction we came from

        return probMap;
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final GrowSignal signal = context.signal();
        int threshold = configuration.get(THICKEN_THRESHOLD);
        Direction newDir = super.selectNewDirection(configuration, context);
        if (signal.isInTrunk() && newDir != Direction.UP) {//Turned out of trunk
            int y = signal.delta.getY();
            boolean extra = y > threshold && y < configuration.getEnergy(context) - threshold;
            signal.energy = 1.5f + (extra?1:0);
            signal.energy = 0.5f;
        }
        return newDir;
    }


    @Override
    public float getEnergy(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        final LevelAccessor world = context.level();
        final BlockPos pos = context.pos();
        float energy = super.getEnergy(configuration, context) + VariateHeightLogic.getHashedVariation(world, pos, configuration.get(HEIGHT_VARIATION));
        if (((int)energy) % 2 == 0) return energy + 1f;
        return energy + 0.5f;
    }
}
