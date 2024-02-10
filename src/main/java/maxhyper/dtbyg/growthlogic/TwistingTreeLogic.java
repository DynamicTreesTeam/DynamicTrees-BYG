package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

import java.util.function.BiConsumer;

public class TwistingTreeLogic extends VariateHeightLogic {

    public static final ConfigurationProperty<Float> CHANCE_TO_SPLIT = ConfigurationProperty.floatProperty("chance_to_split");
    public static final ConfigurationProperty<Integer> DOWN_PROBABILITY = ConfigurationProperty.integer("down_probability");
    public static final ConfigurationProperty<Boolean> SPLIT_ENDS = ConfigurationProperty.bool("split_ends");

    public TwistingTreeLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(CHANCE_TO_SPLIT, 0.01f)
                .with(DOWN_PROBABILITY, 0)
                .with(HEIGHT_VARIATION, 3)
                .with(SPLIT_ENDS, true);
    }

    @Override
    protected void registerProperties() {
        this.register(CHANCE_TO_SPLIT, DOWN_PROBABILITY, HEIGHT_VARIATION, SPLIT_ENDS);
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final LevelAccessor level = context.level();
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        final BlockPos pos = context.pos();
        Direction originDir = signal.dir.getOpposite();

        int count = validSurroundingBranches(level, pos, originDir, (dir, rad)->{
            boolean shouldSplit = level.getRandom().nextFloat() < configuration.get(CHANCE_TO_SPLIT);
            probMap[dir.ordinal()] = rad + (shouldSplit ? 1 : 0);
        });
        //If there are not enough valid branches, just allow any direction except up
        if (count == 0 || (configuration.get(SPLIT_ENDS) && signal.energy < 3)){
            probMap[0] = configuration.get(DOWN_PROBABILITY);
            probMap[1] = context.species().getUpProbability();
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 1;
        }

        probMap[originDir.ordinal()] = 0; // Disable the direction we came from

        return probMap;
    }

    protected int validSurroundingBranches (LevelAccessor level, BlockPos pos, Direction originDir, BiConsumer<Direction, Integer> runForValidDirs){
        int count = 0;
        for (Direction direction : Direction.values()){
            if (direction == originDir) continue;
            int rad = TreeHelper.getRadius(level,pos.offset(direction.getNormal()));
            if (rad > 0) {
                count++;
                runForValidDirs.accept(direction, rad);
            }
        }
        return count;
    }

}
