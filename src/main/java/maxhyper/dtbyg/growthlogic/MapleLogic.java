package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class MapleLogic extends VariateHeightLogic {

    public static final ConfigurationProperty<Boolean> FORCE_UP_AFTER_BRANCHING = ConfigurationProperty.bool("force_up_after_branching");
    public static final ConfigurationProperty<Integer> CANOPY_DEPTH = ConfigurationProperty.integer("canopy_depth");
    public static final ConfigurationProperty<Integer> BRANCHING_HEIGHT = ConfigurationProperty.integer("branching_height");
    public static final ConfigurationProperty<Integer> ZIGZAG_UP_CHANCE = ConfigurationProperty.integer("zigzag_up_chance");

    public MapleLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(FORCE_UP_AFTER_BRANCHING, true)
                .with(HEIGHT_VARIATION, 4)
                .with(CANOPY_DEPTH, 3)
                .with(BRANCHING_HEIGHT, 3)
                .with(ZIGZAG_UP_CHANCE, 5);
    }

    @Override
    protected void registerProperties() {
        this.register(FORCE_UP_AFTER_BRANCHING, HEIGHT_VARIATION, CANOPY_DEPTH, BRANCHING_HEIGHT, ZIGZAG_UP_CHANCE);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();

        int lowestBranch = configuration.getLowestBranchHeight(context);
        int deltaYFromLowest = signal.delta.getY() - lowestBranch;
        int branchingHeight = configuration.get(BRANCHING_HEIGHT);
        // disable down direction
        probMap[0] = 0;
        if (!signal.isInTrunk() && deltaYFromLowest >= 0 && deltaYFromLowest <= branchingHeight) {
            boolean goUp =  CoordUtils.coordHashCode(context.pos(), 2) % configuration.get(ZIGZAG_UP_CHANCE) == 0
                    || (signal.energy % 2 == 0 && configuration.get(FORCE_UP_AFTER_BRANCHING));
            probMap[1] = goUp ? 1 : 0;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = goUp ? 0 : 1;
        } else {
            // disable up in the trunk if the signal is above the branching height, to force branching
            probMap[1] = (signal.isInTrunk() &&
                    deltaYFromLowest >= 0) ||
                    (deltaYFromLowest >= branchingHeight + configuration.get(CANOPY_DEPTH)) ?
                    0 : context.species().getUpProbability();
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 3;
            // If we're not in the trunk, have a small chance of growing up and never grow down
            if (!signal.isInTrunk() && signal.dir != Direction.UP) {
                // Reinforce current growth direction
                // Makes branches more straight to start out with, and then twistier
                int increase = signal.numTurns > 2 ? 0 : 2;
                probMap[signal.dir.ordinal()] += increase;
            }
        }

        // Disable the direction we came from
        Direction originDir = signal.dir.getOpposite();
        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

}
