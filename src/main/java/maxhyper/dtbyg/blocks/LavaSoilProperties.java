package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.data.WaterRootGenerator;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.block.rooty.RootyBlock;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LavaSoilProperties extends SoilProperties {

    public static final TypedRegistry.EntryType<SoilProperties> TYPE = TypedRegistry.newType(LavaSoilProperties::new);

    public LavaSoilProperties(ResourceLocation registryName) {
        super(null, registryName);

        this.soilStateGenerator.reset(WaterRootGenerator::new);
    }

    @Override
    protected RootyBlock createBlock(Block.Properties blockProperties) {
        return new RootyLavaBlock(this, blockProperties);
    }


    @Override
    public Material getDefaultMaterial() {
        return Material.LAVA;
    }

    @Override
    public Block.Properties getDefaultBlockProperties(Material material, MaterialColor materialColor) {
        return Block.Properties.copy(Blocks.LAVA);
    }

    public static class RootyLavaBlock extends RootyBlock implements SimpleLavaloggedBlock {

        protected static final AABB LAVA_ROOTS_AABB = new AABB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9);

        public RootyLavaBlock(SoilProperties properties, Properties blockProperties) {
            super(properties, blockProperties);
            registerDefaultState(defaultBlockState().setValue(SimpleLavaloggedBlock.LAVALOGGED, true));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder.add(LAVALOGGED));
        }

        @Override
        public int getRadiusForConnection(BlockState state, BlockGetter level, BlockPos pos, BranchBlock from, Direction side, int fromRadius) {
            return 1;
        }

        @Override
        public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
            BlockState upState = level.getBlockState(pos.above());
            BranchBlock branch = TreeHelper.getBranch(upState);
            if (branch == null) return ItemStack.EMPTY;
            return branch.getFamily().getBranchItem().map(ItemStack::new).orElse(ItemStack.EMPTY);
        }
        @Override
        public float getHardness(BlockState state, BlockGetter level, BlockPos pos) {
            return (float)(0.5D * DTConfigs.ROOTY_BLOCK_HARDNESS_MULTIPLIER.get());
        }
        @Override
        public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return Shapes.create(LAVA_ROOTS_AABB);
        }
        @Override
        public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
            return Shapes.empty();
        }
        @Override
        public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
            return false;
        }

        @Override
        public FluidState getFluidState(BlockState state) {
            return state.getValue(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(state);
        }

        @SuppressWarnings("depricated")
        @NotNull
        @Override
        public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
            if (pState.getValue(LAVALOGGED)) {
                pLevel.scheduleTick(pCurrentPos, Fluids.LAVA, Fluids.LAVA.getTickDelay(pLevel));
            }
            return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
        }

        @Override
        public BlockState getDecayBlockState(BlockState state, BlockGetter level, BlockPos pos) {
            if (state.hasProperty(LAVALOGGED) && !state.getValue(LAVALOGGED)) {
                return Blocks.AIR.defaultBlockState();
            }
            return super.getDecayBlockState(state, level, pos);
        }

        ///////////////////////////////////////////
        // RENDERING
        ///////////////////////////////////////////

        @Override
        public boolean getColorFromBark() {
            return true;
        }

        @Override
        public boolean fallWithTree(BlockState state, Level level, BlockPos pos) {
            level.setBlockAndUpdate(pos, getDecayBlockState(state, level, pos));
            return true;
        }

        @Override
        public Optional<SoundEvent> getPickupSound() {
            return Fluids.LAVA.getPickupSound();
        }
    }

}
