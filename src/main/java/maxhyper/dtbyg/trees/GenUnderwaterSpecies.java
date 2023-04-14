package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilHelper;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.worldgen.JoCode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class GenUnderwaterSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(GenUnderwaterSpecies::new);

    public GenUnderwaterSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    private static final int maxDepth = 7;
    public boolean isAcceptableSoilForWorldgen(LevelAccessor world, BlockPos pos, BlockState soilBlockState) {
        final boolean isAcceptableSoil = isAcceptableSoil(world, pos, soilBlockState);

        // If the block is water, check the block below it is valid soil (and not water).
        if (isAcceptableSoil && isWater(soilBlockState)) {
            for (int i=1; i<=maxDepth; i++){
                final BlockPos down = pos.below(i);
                final BlockState downState = world.getBlockState(down);

                if (!isWater(downState) && isAcceptableSoilForWorldgen(downState))
                    return true;
            }
            return false;
        }

        return isAcceptableSoil;
    }

    @Override
    public BlockPos preGeneration(LevelAccessor world, BlockPos.MutableBlockPos rootPos, int radius, Direction facing, SafeChunkBounds safeBounds, JoCode joCode) {
        if (this.isWater(world.getBlockState(rootPos))){
            for (int i=1; i<=maxDepth; i++){
                rootPos.move(Direction.DOWN);
                final BlockState downState = world.getBlockState(rootPos);

                if (!isWater(downState) && isAcceptableSoilForWorldgen(downState))
                    break;
            }
        }
        return super.preGeneration(world, rootPos, radius, facing, safeBounds, joCode);
    }

}
