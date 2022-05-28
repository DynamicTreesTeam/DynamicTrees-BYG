package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.network.NodeInspector;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.TrunkShellBlock;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * @author Max Hyper
 */
public class BranchSproutsGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> SPROUT_BLOCK = ConfigurationProperty.property("sprout_block", Block.class);
    public static final ConfigurationProperty<Integer> MIN_RADIUS = ConfigurationProperty.integer("min_radius");

    public BranchSproutsGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(SPROUT_BLOCK,  FRUITING_RADIUS, PLACE_CHANCE, MAX_COUNT, MIN_RADIUS);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(SPROUT_BLOCK, Blocks.AIR)
                .with(FRUITING_RADIUS, 8)
                .with(PLACE_CHANCE, 0.05f)
                .with(MAX_COUNT, 16)
                .with(MIN_RADIUS, 3);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        IWorld world = context.world();
        BlockPos rootPos = context.pos();
        final BlockState blockState = world.getBlockState(rootPos.above());
        final BranchBlock branch = TreeHelper.getBranch(blockState);

        if (branch != null && branch.getRadius(blockState) >= configuration.get(FRUITING_RADIUS)) {
            int count = 1 + world.getRandom().nextInt(configuration.get(MAX_COUNT));
            placeSprouts(count, configuration, world, rootPos);
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
                placeSprouts(1, configuration, world, rootPos);
            }
        }
        return true;
    }

    private void placeSprouts (int count, GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos){
        WeightedList<Pair<BlockPos, Direction>> validSpots = new WeightedList<>();
        final FindSidedBlockNode sproutPlacer = new FindSidedBlockNode(validSpots, configuration.get(MIN_RADIUS));
        TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(sproutPlacer));

        if (!validSpots.isEmpty()){
            for (int i=0; i<count; i++){
                Pair<BlockPos, Direction> selection = validSpots.getOne(world.getRandom());
                BlockPos pos = selection.getFirst();
                Block block = configuration.get(SPROUT_BLOCK);
                if (world.getBlockState(pos.below()).getBlock() == block)
                    return;

                world.setBlock(pos, block.defaultBlockState().setValue(HorizontalBlock.FACING, selection.getSecond()), 3);
            }
        }
    }

    public static class FindSidedBlockNode implements NodeInspector {

        private final WeightedList<Pair<BlockPos, Direction>> validSpots;
        private final int minRadius;

        public FindSidedBlockNode(WeightedList<Pair<BlockPos, Direction>> spots, int minRadius) {
            validSpots = spots;
            this.minRadius = minRadius;
        }

        @Override
        public boolean run(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) {
            int radius = TreeHelper.getRadius(world, pos);
                if (TreeHelper.isBranch(blockState) && radius >= minRadius) {
                    boolean found = false;
                    for (Direction dir : CoordUtils.HORIZONTALS){
                        BlockPos offsetPos = pos.offset(dir.getNormal());
                        BlockState offsetState = world.getBlockState(offsetPos);
                        if (offsetState.getMaterial() == Material.AIR || offsetState.getBlock() instanceof TrunkShellBlock){
                            validSpots.add(new Pair<>(offsetPos, dir), radius);
                            found = true;
                        }
                    }
                    return found;
                }
            return false;
        }

        @Override
        public boolean returnRun(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) { return false;}

    }


}
