package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.branch.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class DiagonalPalmFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(DiagonalPalmFamily::new);

    public DiagonalPalmFamily(ResourceLocation name) {
        super(name);
    }
    
    @Override
    protected BranchBlock createBranchBlock(ResourceLocation name) {
        final BasicBranchBlock branch = new BasicBranchBlock(name, this.getProperties()){

            @Override
            public GrowSignal growIntoAir(Level world, BlockPos pos, GrowSignal signal, int fromRadius) {
                final Species species = signal.getSpecies();

                final DynamicLeavesBlock leaves = species.getLeavesBlock().orElse(null);
                if (leaves != null) {
                    if (fromRadius == getFamily().getPrimaryThickness()) {// If we came from a twig (and we're not a stripped branch) then just make some leaves
                        if (isNextToBranch(world, pos, signal.dir.getOpposite())){
                            signal.success = false;
                            return signal;
                        }
                        signal.success = leaves.growLeavesIfLocationIsSuitable(world, species.getLeavesProperties(), pos.above(), 0);
                        if (signal.success)
                            return leaves.branchOut(world, pos, signal);
                    } else {// Otherwise make a proper branch
                        return leaves.branchOut(world, pos, signal);
                    }
                } else {
                    //If the leaves block is null, the branch grows directly without checking for leaves requirements
                    if (isNextToBranch(world, pos, signal.dir.getOpposite())){
                        signal.success = false;
                        return signal;
                    }
                    setRadius(world, pos, getFamily().getPrimaryThickness(), null);
                    signal.radius = getFamily().getSecondaryThickness();
                    signal.success = true;
                }
                return signal;
            }
        };
        if (this.isFireProof())
            branch.setFireSpreadSpeed(0).setFlammability(0);
        return branch;
    }
}
