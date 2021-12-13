package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.TrunkShellBlock;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import javafx.util.Pair;
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
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.LinkedList;
import java.util.List;

public class BranchSproutsGenFeature extends GenFeature implements IPostGenFeature, IPostGrowFeature {

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
    public ConfiguredGenFeature<GenFeature> createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(SPROUT_BLOCK, Blocks.AIR)
                .with(FRUITING_RADIUS, 8)
                .with(PLACE_CHANCE, 0.02f)
                .with(MAX_COUNT, 16)
                .with(MIN_RADIUS, 0);
    }

    @Override
    public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
        final BlockState blockState = world.getBlockState(rootPos);
        final BranchBlock branch = TreeHelper.getBranch(blockState);

        if (branch != null && branch.getRadius(blockState) >= configuredGenFeature.get(FRUITING_RADIUS)) {
            int count = 1 + world.getRandom().nextInt(configuredGenFeature.get(MAX_COUNT));
            placeSprouts(count, configuredGenFeature,world,rootPos);
        }
        return true;
    }

    @Override
    public boolean postGrow(ConfiguredGenFeature<?> configuredGenFeature, World world, BlockPos rootPos, BlockPos treePos, Species species, int fertility, boolean natural) {
        if (fertility == 0) return false;
        final BlockState blockState = world.getBlockState(treePos);
        final BranchBlock branch = TreeHelper.getBranch(blockState);

        if (branch != null && branch.getRadius(blockState) >= configuredGenFeature.get(FRUITING_RADIUS) && natural) {
            if (world.getRandom().nextFloat() < configuredGenFeature.get(PLACE_CHANCE)) {
                placeSprouts(1, configuredGenFeature,world,rootPos);
            }
        }
        return true;
    }

    private void placeSprouts (int count, ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos){
        WeightedList<Pair<BlockPos, Direction>> validSpots = new WeightedList<>();
        final FindSidedBlockNode sproutPlacer = new FindSidedBlockNode(validSpots, configuredGenFeature.get(MIN_RADIUS));
        TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(sproutPlacer));

        if (!validSpots.isEmpty()){
            Pair<BlockPos, Direction> selection = validSpots.getOne(world.getRandom());
            for (int i=0; i<count; i++)
                world.setBlock(selection.getKey(), configuredGenFeature.get(SPROUT_BLOCK).defaultBlockState().setValue(HorizontalBlock.FACING, selection.getValue()), 3);
        }
    }

    public static class FindSidedBlockNode implements INodeInspector {

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
                    for (Direction dir : CoordUtils.HORIZONTALS){
                        BlockPos offsetPos = pos.offset(dir.getNormal());
                        BlockState offsetState = world.getBlockState(offsetPos);
                        if (offsetState.getMaterial() == Material.AIR || offsetState.getBlock() instanceof TrunkShellBlock){
                            validSpots.add(new Pair<>(offsetPos, dir), radius);
                            return true;
                        }
                    }
                }
            return false;
        }

        @Override
        public boolean returnRun(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) { return false;}

    }


}
