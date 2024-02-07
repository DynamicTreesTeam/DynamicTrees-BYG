package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.network.NodeInspector;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;

import java.util.stream.Collectors;

/**
 * @author Max Hyper
 */
public class AlternativeBranchGenFeature extends GenFeature {

    public static final ConfigurationProperty<Float> WORLDGEN_PLACE_CHANCE = ConfigurationProperty.floatProperty("worldgen_place_chance");
    public static final ConfigurationProperty<Block> ALT_BRANCH_BLOCK = ConfigurationProperty.block("alternative_branch_block");
    public static final ConfigurationProperty<Integer> MIN_RADIUS = ConfigurationProperty.integer("minimum_radius");

    public AlternativeBranchGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {
        Block branch = configuration.get(ALT_BRANCH_BLOCK);
        if (TreeHelper.isBranch(branch) && species.getFamily().isValidBranchBlock((BranchBlock) branch)){
            return true;
        }
        LogManager.getLogger().warn("Failed to find branch block for the alternative branch feature on species {}", species);
        return false;
    }

    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(ALT_BRANCH_BLOCK, Blocks.AIR)
                .with(PLACE_CHANCE, 0.02f)
                .with(WORLDGEN_PLACE_CHANCE, 0.1f)
                .with(MIN_RADIUS, 4)
                .with(FRUITING_RADIUS, 6);
    }

    @Override
    protected void registerProperties() {
        this.register(ALT_BRANCH_BLOCK, PLACE_CHANCE, WORLDGEN_PLACE_CHANCE, MIN_RADIUS, FRUITING_RADIUS);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        LevelAccessor level = context.level();
        BlockPos rootPos = context.pos();
        final BlockState blockState = level.getBlockState(rootPos.above());
        final BranchBlock branch = TreeHelper.getBranch(blockState);

        if (branch != null && branch.getRadius(blockState) >= configuration.get(FRUITING_RADIUS)) {
            placeAltBranches(true, configuration, level, rootPos, context.species().getFamily());
        }
        return true;
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if (context.fertility() == 0) return false;

        LevelAccessor level = context.level();
        BlockPos rootPos = context.pos();
        final BlockState blockState = level.getBlockState(rootPos.above());
        final BranchBlock branch = TreeHelper.getBranch(blockState);

        if (branch != null && branch.getRadius(blockState) >= configuration.get(FRUITING_RADIUS) && context.natural()) {
            if (level.getRandom().nextFloat() < configuration.get(PLACE_CHANCE)) {
                placeAltBranches(false, configuration, level, rootPos, context.species().getFamily());
            }
        }
        return true;
    }

    private void placeAltBranches(boolean isWorldgen, GenFeatureConfiguration configuration, LevelAccessor world, BlockPos rootPos, Family family){
        SimpleWeightedRandomList.Builder<BlockPos> listBuilder = new SimpleWeightedRandomList.Builder<>();
        final FindValidBranchesNode altBranchPlacer = new FindValidBranchesNode(listBuilder, configuration.get(MIN_RADIUS), family);
        TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(altBranchPlacer));
        SimpleWeightedRandomList<BlockPos> validSpots = listBuilder.build();

        if (!validSpots.isEmpty()) {
            if (isWorldgen){
                for (BlockPos listPos : validSpots.unwrap().stream().map(WeightedEntry.Wrapper::getData).collect(Collectors.toList()))
                    if (world.getRandom().nextFloat() < configuration.get(WORLDGEN_PLACE_CHANCE))
                        placeBranch(configuration, world, listPos);
            } else {
                WeightedEntry.Wrapper<BlockPos> posWrapper = validSpots.getRandom(world.getRandom()).orElse(null);
                if (posWrapper == null) return;
                placeBranch(configuration, world, posWrapper.getData());
            }

        }

    }

    private void placeBranch (GenFeatureConfiguration configuration, LevelAccessor world, BlockPos pos){
        BranchBlock branchToPlace = (BranchBlock) configuration.get(ALT_BRANCH_BLOCK);
        int radius = TreeHelper.getRadius(world, pos);

        branchToPlace.setRadius(world, pos, radius, null);
    }

    public static class FindValidBranchesNode implements NodeInspector {

        private final SimpleWeightedRandomList.Builder<BlockPos> validSpots;
        private final int minRadius;
        private final Family family;

        public FindValidBranchesNode(SimpleWeightedRandomList.Builder<BlockPos> validSpots, int minRadius, Family family) {
            this.validSpots = validSpots;
            this.minRadius = minRadius;
            this.family = family;
        }

        @Override
        public boolean run(BlockState state, LevelAccessor level, BlockPos pos, Direction fromDir) {
            int radius = TreeHelper.getRadius(level, pos);
            boolean valid = radius >= minRadius && state.getBlock() == family.getValidBranchBlock(0);
            if (valid) validSpots.add(pos, radius);
            return valid;
        }

        @Override
        public boolean returnRun(BlockState state, LevelAccessor level, BlockPos pos, Direction fromDir) {
            return false;
        }
    }

}
