package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.DynamicSaplingBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
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

public class MangroveSpecies extends Species {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(MangroveSpecies::new);

    public MangroveSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }

    @Override
    public Species generateSeed() {
        return !this.shouldGenerateSeed() || this.seed != null ? this :
                this.setSeed(RegistryHandler.addItem(getSeedName(), new Seed(this){

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

        if (sapling != null && fluidState.getType() == Fluids.WATER && fluidStateUp.getType() == Fluids.EMPTY){
            world.setBlock(pos.above(), sapling.defaultBlockState(), 3);
            return true;
        }

        return super.plantSapling(world, pos, locationOverride);
    }

}
