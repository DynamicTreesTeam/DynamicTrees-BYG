package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WillowLogic extends VariateHeightLogic {

    public static final ConfigurationProperty<Integer> CANOPY_DEPTH = ConfigurationProperty.integer("canopy_depth");

    public WillowLogic(ResourceLocation registryName) { super(registryName); }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(CANOPY_DEPTH, 4)
                .with(HEIGHT_VARIATION, 8);
    }

    @Override
    protected void registerProperties() {
        this.register(CANOPY_DEPTH, HEIGHT_VARIATION);
    }


    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();

        final Direction originDir = signal.dir.getOpposite();

        probMap[Direction.DOWN.ordinal()] = 2;

        int lowestBranch = configuration.getLowestBranchHeight(context);
        if (signal.delta.getY() >= lowestBranch + configuration.get(CANOPY_DEPTH))
            probMap[Direction.UP.ordinal()] = 0;

        probMap[originDir.ordinal()] = 0;

        return probMap;
    }
}
