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
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class MangroveSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(MangroveSpecies::new);

    public MangroveSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public Species generateSeed() {
        return !this.shouldGenerateSeed() || this.seed != null ? this :
                this.setSeed(RegistryHandler.addItem(getSeedName(), new Seed(this) {

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

        if (sapling != null && fluidState.getType() == Fluids.WATER && fluidStateUp.getType() == Fluids.EMPTY) {
            return super.plantSapling(world, pos.above(), locationOverride);
        }

        return super.plantSapling(world, pos, locationOverride);
    }

    private static final int maxDepth = 5;
    private static final int maxOffset = 4;

    public boolean isAcceptableSoilForWorldgen(IWorld world, BlockPos pos, BlockState soilBlockState) {
        final boolean isAcceptableSoil = isAcceptableSoil(world, pos, soilBlockState);

        // If the block is water, check the block below it is valid soil (and not water).
        if (isAcceptableSoil && isWater(soilBlockState)) {
            for (int i = 1; i <= maxDepth; i++) {
                final BlockPos down = pos.below(i);
                final BlockState downState = world.getBlockState(down);

                if (!isWater(downState) && this.isAcceptableSoil(world, down, downState))
                    return true;
            }
            return false;
        }


        return isAcceptableSoil;
    }

    @Override
    public boolean generate(WorldContext worldContext, BlockPos rootPos, Biome biome, Random random, int radius, SafeChunkBounds safeBounds) {
        int i;
        for (i = 0; i < maxOffset; i++) {
            if (!isWater(worldContext.access().getBlockState(rootPos.below(i)))) {
                break;
            }
        }
        return super.generate(worldContext, rootPos.below(Math.max(i - 2, 0)), biome, random, radius, safeBounds);
    }
}
