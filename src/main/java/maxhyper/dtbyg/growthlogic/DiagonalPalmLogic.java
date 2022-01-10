package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.PalmGrowthLogic;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.MathHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DiagonalPalmLogic extends PalmGrowthLogic {

    public static final ConfigurationProperty<Float> CHANCE_TO_DIVERGE = ConfigurationProperty.floatProperty("chance_to_diverge");
    public static final ConfigurationProperty<Float> CHANCE_TO_SPLIT = ConfigurationProperty.floatProperty("chance_to_split");
    public static final ConfigurationProperty<Float> SPLIT_MAX_ENERGY_FACTOR = ConfigurationProperty.floatProperty("split_max_energy_factor");

    public DiagonalPalmLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(CHANCE_TO_DIVERGE, 0.8f)
                .with(CHANCE_TO_SPLIT, 0.06f)
                .with(SPLIT_MAX_ENERGY_FACTOR, 0.5f); //can only split under the bottom half
    }

    @Override
    protected void registerProperties() {
        this.register(CHANCE_TO_DIVERGE, CHANCE_TO_SPLIT, SPLIT_MAX_ENERGY_FACTOR);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final Species species = context.species();
        final World world = context.world();
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        final BlockPos pos = context.pos();
        Direction originDir = signal.dir.getOpposite();

        // Alter probability map for direction change
        probMap[0] = 0; // Down is always disallowed for palm
        probMap[1] = species.getUpProbability();
        // Start by disabling probability on the sides
        probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;

        int diverge = (int)(4/configuration.get(CHANCE_TO_DIVERGE));
        int split = (int)(1/configuration.get(CHANCE_TO_SPLIT));
        int randCoordCode = Math.abs(CoordUtils.coordHashCode(pos, 2));

        int directionSelection = randCoordCode % diverge;
        int splitSelection = randCoordCode % split;
        if (directionSelection < 4 && signal.energy > 1){
            Direction selectedDir = Direction.values()[2 + directionSelection];
            //only do branching if it just grew up (to avoid long sideways branches)
            if (originDir == Direction.DOWN){
                probMap[selectedDir.ordinal()] = 10;
                //if the chance to split is met, the clockwise direction is also enabled
                if (splitSelection == 0 && signal.energy > species.getEnergy(world, signal.rootPos) *
                        Math.max(0, Math.min(1, 1 - configuration.get(SPLIT_MAX_ENERGY_FACTOR)))){
                    probMap[selectedDir.getClockWise().ordinal()] = 10;
                }
                probMap[1] = 0;
            }
        }

        probMap[originDir.ordinal()] = 0; // Disable the direction we came from

        return probMap;
    }

}
