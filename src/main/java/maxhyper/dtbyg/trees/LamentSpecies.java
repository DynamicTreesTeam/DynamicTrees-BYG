package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.DynamicSaplingBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.WorldContext;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

/**
 * This species will place another alternative species,
 * if the soil is acceptable for the alternative species.
 *
 * @author Max Hyper
 */
public class LamentSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(LamentSpecies::new);

    private Species altSpecies = Species.NULL_SPECIES;

    public void setAltSpecies(Species altSpecies) {
        if (altSpecies != this)
            this.altSpecies = altSpecies;
    }

    public LamentSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public boolean generate(WorldContext worldContext, BlockPos rootPos, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
        if (altSpecies.isAcceptableSoilForWorldgen(worldContext.level(), rootPos, worldContext.access().getBlockState(rootPos))) {
            return altSpecies.generate(worldContext, rootPos, biome, random, radius, safeBounds);
        }
        return super.generate(worldContext, rootPos, biome, random, radius, safeBounds);
    }

    @Override
    public boolean isAcceptableSoil(IWorldReader world, BlockPos pos, BlockState soilBlockState) {
        return super.isAcceptableSoil(world, pos, soilBlockState) || altSpecies.isAcceptableSoil(world, pos, soilBlockState);
    }

    @Override
    protected boolean transitionToTree(World world, BlockPos pos, Family family) {
        if (altSpecies.isAcceptableSoil(world, pos.below(), world.getBlockState(pos.below()))) {
            return altSpecies.transitionToTree(world, pos);
        }
        return super.transitionToTree(world, pos);
    }

    @Override
    public Species generateSeed() {
        return !this.shouldGenerateSeed() || this.seed != null ? this :
                this.setSeed(RegistryHandler.addItem(getSeedName(), new Seed(this){
                    @Override
                    public boolean isFireResistant() { return true; }
                    @Override
                    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
                        BlockRayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
                        BlockRayTraceResult rayTraceResultUp = rayTraceResult.withPosition(rayTraceResult.getBlockPos().above());
                        ActionResultType actionresulttype = super.useOn(new ItemUseContext(player, hand, rayTraceResult.getDirection() == Direction.UP ? rayTraceResultUp : rayTraceResult));
                        return new ActionResult<>(actionresulttype, player.getItemInHand(hand));
                    }
                }));
    }

    @Override
    public boolean plantSapling(IWorld world, BlockPos pos, boolean locationOverride) {
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidStateUp = world.getFluidState(pos.above());

        final DynamicSaplingBlock sapling = this.getSapling().orElse(null);

        if (sapling != null && fluidState.getType() == Fluids.LAVA && fluidStateUp.getType() == Fluids.EMPTY){
            return super.plantSapling(world, pos.above(), locationOverride);
        }

        return super.plantSapling(world, pos, locationOverride);
    }

}
