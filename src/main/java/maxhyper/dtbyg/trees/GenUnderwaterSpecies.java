package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilHelper;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class GenUnderwaterSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(GenUnderwaterSpecies::new);

    public GenUnderwaterSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    private static final int maxDepth = 7;
    public boolean isAcceptableSoilForWorldgen(IWorld world, BlockPos pos, BlockState soilBlockState) {
        final boolean isAcceptableSoil = isAcceptableSoil(world, pos, soilBlockState);

        // If the block is water, check the block below it is valid soil (and not water).
        if (isAcceptableSoil && isWater(soilBlockState)) {
            for (int i=1; i<=maxDepth; i++){
                final BlockPos down = pos.below(i);
                final BlockState downState = world.getBlockState(down);

                if (!isWater(downState) && isAcceptableSoilUnderWater(downState))
                    return true;
            }
            return false;
        }

        return isAcceptableSoil;
    }

    @Override
    public BlockPos preGeneration(IWorld world, BlockPos rootPosition, int radius, Direction facing, SafeChunkBounds safeBounds, JoCode joCode) {
        BlockPos root = rootPosition;
        if (this.isWater(world.getBlockState(rootPosition))){
            int i=1;
            for (; i<=maxDepth; i++){
                final BlockPos down = rootPosition.below(i);
                final BlockState downState = world.getBlockState(down);

                if (!isWater(downState) && isAcceptableSoilUnderWater(downState))
                    break;
            }
            root = root.below(i);
        }
        return super.preGeneration(world, root, radius, facing, safeBounds, joCode);
    }

    public boolean isAcceptableSoilUnderWater(BlockState soilBlockState) {
        return SoilHelper.isSoilAcceptable(soilBlockState.getBlockState(), this.soilTypeFlags | SoilHelper.getSoilFlags("sand_like", "mud_like"));
    }

}
