package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class MegaRainbowEucalyptusLogic extends GrowthLogicKit {
    public MegaRainbowEucalyptusLogic(ResourceLocation registryName) {
        super(registryName);
    }

    public static final ConfigurationProperty<Float> CANOPY_HEIGHT_FACTOR = ConfigurationProperty.floatProperty("canopy_height_factor");
    public static final ConfigurationProperty<Float> SPLIT_HEIGHT_FACTOR = ConfigurationProperty.floatProperty("split_height_factor");

    private static final float canopyHeightFraction = 0.5f;
    private static final float splitHeightFraction = 0.4f;

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(CANOPY_HEIGHT_FACTOR, 0.5f)
                .with(SPLIT_HEIGHT_FACTOR, 0.4f);
    }

    @Override
    protected void registerProperties() {
        this.register(CANOPY_HEIGHT_FACTOR, SPLIT_HEIGHT_FACTOR);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        float energy = configuration.getEnergy(context);
        if ((signal.delta.getY() > energy * splitHeightFraction && signal.isInTrunk()) ||
                signal.delta.getY() > energy * canopyHeightFraction)
            probMap[Direction.UP.ordinal()] = 0;
        return probMap;
    }
}
