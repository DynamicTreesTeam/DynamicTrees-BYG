package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.DynamicSaplingBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.item.Seed;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class MangroveSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(MangroveSpecies::new);

    public MangroveSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public Species generateSeed() {
        return !this.shouldGenerateSeed() || this.seed != null ? this :
                this.setSeed(RegistryHandler.addItem(getSeedName(), ()->new Seed(this)
//                        {
//
//                            @Override
//                            public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
//                                BlockRayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
//                                BlockRayTraceResult rayTraceResultUp = rayTraceResult.withPosition(rayTraceResult.getBlockPos().above());
//                                ActionResultType actionresulttype = super.useOn(new ItemUseContext(player, hand, rayTraceResult.getDirection() == Direction.UP ? rayTraceResultUp : rayTraceResult));
//                                return new ActionResult<>(actionresulttype, player.getItemInHand(hand));
//                            }
//                        }
                ));
    }

    @Override
    public boolean plantSapling(LevelAccessor world, BlockPos pos, boolean locationOverride) {
        FluidState fluidState = world.getFluidState(pos);
        FluidState fluidStateUp = world.getFluidState(pos.above());

        final DynamicSaplingBlock sapling = this.getSapling().orElse(null);

        if (sapling != null && fluidState.getType() == Fluids.WATER && fluidStateUp.getType() == Fluids.EMPTY){
            return super.plantSapling(world, pos.above(), locationOverride);
        }

        return super.plantSapling(world, pos, locationOverride);
    }

    private static final int maxDepth = 5;
    private static final int maxOffset = 4;

    @Override
    public boolean isAcceptableSoilForWorldgen(LevelAccessor world, BlockPos pos, BlockState soilBlockState) {
        final boolean isAcceptableSoil = isAcceptableSoil(world, pos, soilBlockState);

        // If the block is water, check the block below it is valid soil (and not water).
        if (isAcceptableSoil && isWater(soilBlockState)) {
            for (int i=1; i<=maxDepth; i++){
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
    public boolean generate(GenerationContext context) {
        int i;
        for (i=0; i<maxOffset; i++)
            if (!isWater(context.level().getBlockState(context.rootPos().below(i))))
                break;
        return super.generate(context);
    }

}
