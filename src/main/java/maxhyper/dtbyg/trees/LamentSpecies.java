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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

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
    public boolean generate(GenerationContext context) {
        LevelAccessor level = context.level();
        BlockPos rootPos = context.rootPos();
        if (altSpecies.isAcceptableSoilForWorldgen(level, rootPos, level.getBlockState(rootPos)))
            return altSpecies.generate(context);
        return super.generate(context);
    }

    @Override
    public boolean isAcceptableSoilForWorldgen(LevelAccessor level, BlockPos pos, BlockState soilBlockState) {
        return super.isAcceptableSoilForWorldgen(level, pos, soilBlockState) || altSpecies.isAcceptableSoilForWorldgen(level, pos, soilBlockState);
    }
    @Override
    public boolean isAcceptableSoil(LevelReader level, BlockPos pos, BlockState soilBlockState) {
        return super.isAcceptableSoil(level, pos, soilBlockState) || altSpecies.isAcceptableSoil(level, pos, soilBlockState);
    }

    @Override
    protected boolean transitionToTree(Level level, BlockPos pos, Family family) {
        if (altSpecies.isAcceptableSoil(level, pos.below(), level.getBlockState(pos.below())))
            return altSpecies.transitionToTree(level, pos);
        return super.transitionToTree(level, pos, family);
    }

    @Override
    public Species generateSeed() {
        return !this.shouldGenerateSeed() || this.seed != null ? this :
                this.setSeed(RegistryHandler.addItem(getSeedName(), ()->new Seed(this){
                            @Override
                            public boolean isFireResistant() {
                                return true;
                            }

//                            @Override
//                            public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
//                                BlockRayTraceResult rayTraceResult = getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
//                                BlockRayTraceResult rayTraceResultUp = rayTraceResult.withPosition(rayTraceResult.getBlockPos().above());
//                                ActionResultType actionresulttype = super.useOn(new ItemUseContext(player, hand, rayTraceResult.getDirection() == Direction.UP ? rayTraceResultUp : rayTraceResult));
//                                return new ActionResult<>(actionresulttype, player.getItemInHand(hand));
//                            }
                        }
                ));
    }

    @Override
    public boolean plantSapling(LevelAccessor level, BlockPos pos, boolean locationOverride) {
        FluidState fluidState = level.getFluidState(pos);
        FluidState fluidStateUp = level.getFluidState(pos.above());

        final DynamicSaplingBlock sapling = this.getSapling().orElse(null);

        if (sapling != null && fluidState.getType() == Fluids.LAVA && fluidStateUp.getType() == Fluids.EMPTY){
            return super.plantSapling(level, pos.above(), locationOverride);
        }
        return super.plantSapling(level, pos, locationOverride);
    }

}
