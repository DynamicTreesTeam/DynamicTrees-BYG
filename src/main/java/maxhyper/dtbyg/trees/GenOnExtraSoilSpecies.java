package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilHelper;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class GenOnExtraSoilSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(GenOnExtraSoilSpecies::new);

    private Block extraSoil;
    private Block soilReplacement;

    public GenOnExtraSoilSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    public void setExtraSoil(Block extraSoil) {
        this.extraSoil = extraSoil;
    }

    public void setSoilReplacement(Block soilReplacement) {
        this.soilReplacement = soilReplacement;
    }

    @Override
    public boolean isAcceptableSoilForWorldgen(LevelAccessor world, BlockPos pos, BlockState soilBlockState) {
        if (soilBlockState.is(extraSoil))
            return true;
        return super.isAcceptableSoilForWorldgen(world, pos, soilBlockState);
    }

    @Override
    public boolean placeRootyDirtBlock(LevelAccessor world, BlockPos rootPos, int fertility) {
        if (world.getBlockState(rootPos).is(extraSoil)) {
            SoilHelper.getProperties(soilReplacement).getBlock().ifPresent(
                    rootyBlock -> world.setBlock(rootPos, rootyBlock.defaultBlockState(), 3)
            );
        }
        return super.placeRootyDirtBlock(world, rootPos, fertility);
    }

}
