package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

public class EtherTreeLogic extends TwistingTreeLogic{

    public static final ConfigurationProperty<Float> CANOPY_SIDE_ENERGY = ConfigurationProperty.floatProperty("canopy_side_energy");
    public static final ConfigurationProperty<Integer> HEIGHT_BETWEEN_CANOPY_LAYERS = ConfigurationProperty.integer("height_between_canopy_layers");

    public EtherTreeLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(CHANCE_TO_SPLIT, 0f)
                .with(HEIGHT_VARIATION, 30)
                .with(CANOPY_SIDE_ENERGY, 15f)
                .with(HEIGHT_BETWEEN_CANOPY_LAYERS, 8);
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        this.register(CANOPY_SIDE_ENERGY, HEIGHT_BETWEEN_CANOPY_LAYERS);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final LevelAccessor world = context.level();
        final BlockPos pos = context.pos();
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();

        float maxEnergy = configuration.getEnergy(context);
        int HBCL = configuration.get(HEIGHT_BETWEEN_CANOPY_LAYERS);
        //int layers = ((int)maxEnergy - HBCL) % HBCL;
        if (signal.delta.getY() % HBCL == 0){
            probMap[0] = configuration.get(DOWN_PROBABILITY);
            probMap[1] = TreeHelper.isBranch(world.getBlockState(pos.below())) ? context.species().getUpProbability() : 0;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 1;

            probMap[signal.dir.getOpposite().ordinal()] = 0; // Disable the direction we came from

            return probMap;
        } else return super.populateDirectionProbabilityMap(configuration, context);
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final Direction newDir = super.selectNewDirection(configuration, context);

        final GrowSignal signal = context.signal();

        int HBCL = configuration.get(HEIGHT_BETWEEN_CANOPY_LAYERS);
        if (signal.delta.getY() % HBCL == 0 && newDir != Direction.UP){
            signal.energy = Math.max(signal.energy, configuration.get(CANOPY_SIDE_ENERGY));
        }
        return newDir;
    }
}
