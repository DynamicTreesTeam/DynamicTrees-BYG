package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilHelper;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import corgiaoc.byg.core.BYGBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * Allows generation of trees on ether stone, replacing the soil
 * with vermilion sculk
 */
public class EtherSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(EtherSpecies::new);

    public EtherSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public boolean isAcceptableSoilForWorldgen(IWorld world, BlockPos pos, BlockState soilBlockState) {
        if (soilBlockState.is(BYGBlocks.ETHER_STONE))
            return true;
        return super.isAcceptableSoilForWorldgen(world, pos, soilBlockState);
    }

    @Override
    public boolean placeRootyDirtBlock(IWorld world, BlockPos rootPos, int fertility) {
        if (world.getBlockState(rootPos).is(BYGBlocks.ETHER_STONE)) {
            RootyBlock rootyBlock = SoilHelper.getProperties(BYGBlocks.VERMILION_SCULK).getDynamicSoilBlock();
            if (rootyBlock != null)
                world.setBlock(rootPos, rootyBlock.defaultBlockState(), 3);//the super does the rest of setting up the soil
        }
        return super.placeRootyDirtBlock(world, rootPos, fertility);
    }
}