package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.network.NodeInspector;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
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
        IWorld world = context.world();
        BlockPos rootPos = context.pos();
        final BlockState blockState = world.getBlockState(rootPos.above());
        final BranchBlock branch = TreeHelper.getBranch(blockState);

        if (branch != null && branch.getRadius(blockState) >= configuration.get(FRUITING_RADIUS)) {
            placeAltBranches(true, configuration, world, rootPos, context.species().getFamily());
        }
        return true;
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if (context.fertility() == 0) return false;

        IWorld world = context.world();
        BlockPos rootPos = context.pos();
        final BlockState blockState = world.getBlockState(rootPos.above());
        final BranchBlock branch = TreeHelper.getBranch(blockState);

        if (branch != null && branch.getRadius(blockState) >= configuration.get(FRUITING_RADIUS) && context.natural()) {
            if (world.getRandom().nextFloat() < configuration.get(PLACE_CHANCE)) {
                placeAltBranches(false, configuration, world, rootPos, context.species().getFamily());
            }
        }
        return true;
    }

    private void placeAltBranches(boolean isWorldgen, GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos, Family family){
        WeightedList<BlockPos> validSpots = new WeightedList<>();
        final FindValidBranchesNode altBranchPlacer = new FindValidBranchesNode(validSpots, configuration.get(MIN_RADIUS), family);
        TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(altBranchPlacer));

        if (!validSpots.isEmpty()) {
            if (isWorldgen){
                for (BlockPos listPos : validSpots.stream().collect(Collectors.toSet()))
                    if (world.getRandom().nextFloat() < configuration.get(WORLDGEN_PLACE_CHANCE))
                        placeBranch(configuration, world, listPos);
            } else
                placeBranch(configuration, world, validSpots.getOne(world.getRandom()));
        }

    }

    private void placeBranch (GenFeatureConfiguration configuration, IWorld world, BlockPos pos){
        BranchBlock branchToPlace = (BranchBlock) configuration.get(ALT_BRANCH_BLOCK);
        int radius = TreeHelper.getRadius(world, pos);

        branchToPlace.setRadius(world, pos, radius, null);
    }

    public static class FindValidBranchesNode implements NodeInspector {

        private final WeightedList<BlockPos> validSpots;
        private final int minRadius;
        private final Family family;

        public FindValidBranchesNode(WeightedList<BlockPos> validSpots, int minRadius, Family family) {
            this.validSpots = validSpots;
            this.minRadius = minRadius;
            this.family = family;
        }

        @Override
        public boolean run(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) {
            int radius = TreeHelper.getRadius(world, pos);
            boolean valid = radius >= minRadius && blockState.getBlock() == family.getValidBranchBlock(0);
            if (valid) validSpots.add(pos, radius);
            return valid;
        }

        @Override
        public boolean returnRun(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) { return false; }
    }

}
