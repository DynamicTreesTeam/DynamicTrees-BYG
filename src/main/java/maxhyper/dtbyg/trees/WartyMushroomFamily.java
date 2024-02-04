package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.branch.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.BlockBounds;
import com.ferreusveritas.dynamictrees.util.BlockStates;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.MushroomBranchBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.context.MushroomCapContext;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomFamily;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomSpecies;
import maxhyper.dtbyg.blocks.WartyCapProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WartyMushroomFamily extends HugeMushroomFamily {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(WartyMushroomFamily::new);

    public WartyMushroomFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    protected BranchBlock createBranchBlock(ResourceLocation name) {
        BasicBranchBlock branch = new MushroomBranchBlock(name, this.getProperties()){
            //Method is called destroy leaves but this one is to destroy mushroom caps
            public void destroyMushroomCap(final @NotNull Level level, final @NotNull BlockPos cutPos, final @NotNull Species species, final @NotNull ItemStack tool, final @NotNull List<BlockPos> endPoints, final @NotNull Map<BlockPos, BlockState> destroyedCapBlocks, final @NotNull List<ItemStackPos> drops) {
                if (!(species instanceof final HugeMushroomSpecies mushSpecies)) return;
                if (!(species.getFamily() instanceof final HugeMushroomFamily family)) return;

                if (level.isClientSide || endPoints.isEmpty()) {
                    return;
                }

                // Make a bounding volume that holds all the endpoints and expand the volume for the leaves' radius.
                final BlockBounds bounds = getFamily().expandLeavesBlockBounds(new BlockBounds(endPoints));

                // Create a voxmap to store the leaf destruction map.
                final SimpleVoxmap capMap = new SimpleVoxmap(bounds);

                // For each of the endpoints add an expanded destruction volume around it.
                for (final BlockPos endPos : endPoints) {
                    int age = DynamicCapCenterBlock.getCapAge(level, endPos.above());
                    if (age >= 0){
                        for (final BlockPos findPos : mushSpecies.getMushroomShapeKit().getShapeCluster(new MushroomCapContext(level, endPos.above(), mushSpecies, age))) {
                            final BlockState findState = level.getBlockState(findPos);
                            if (family.isCompatibleCap(mushSpecies, findState, level, findPos)) { // Search for endpoints of the same tree family.
                                capMap.setVoxel(findPos.getX(), findPos.getY(), findPos.getZ(), (byte) 1); // Flag this position for destruction.
                            }
                        }
                        capMap.setVoxel(endPos, (byte) 0); // We know that the endpoint does not have a leaves block in it because it was a branch.
                    }
                }

                final List<ItemStack> dropList = new ArrayList<>();

                // Destroy all family compatible leaves.
                for (final SimpleVoxmap.Cell cell : capMap.getAllNonZeroCells()) {
                    final BlockPos.MutableBlockPos pos = cell.getPos();
                    final BlockState state = level.getBlockState(pos);
                    if (family.isCompatibleCap(mushSpecies, state, level, pos)) {
                        dropList.clear();
                        CapProperties cap = getCapProperties(state);
                        Block shroomlightBlock = null;
                        if (cap instanceof WartyCapProperties wartyCap){
                            shroomlightBlock = wartyCap.getShroomlightBlock();
                        }
                        dropList.addAll(cap.getDrops(level, pos, tool, species));
                        final BlockPos imPos = pos.immutable(); // We are storing this so it must be immutable
                        final BlockPos relPos = imPos.subtract(cutPos);
                        level.setBlock(imPos, BlockStates.AIR, 3);
                        if (shroomlightBlock != null){
                            BlockState upState = level.getBlockState(imPos.above());
                            BlockState downState = level.getBlockState(imPos.below());
                            if (upState.is(shroomlightBlock))
                                level.destroyBlock(imPos.above(), true);
                            if (downState.is(shroomlightBlock)){
                                level.destroyBlock(imPos.below(), true);
                            }
                        }
                        destroyedCapBlocks.put(relPos, state);
                        dropList.forEach(i -> drops.add(new ItemStackPos(i, relPos)));
                    }
                }
            }

            private CapProperties getCapProperties(BlockState state) {
                if (state.getBlock() instanceof DynamicCapBlock) {
                    return Optional.of((DynamicCapBlock)state.getBlock()).map((block) -> block.getProperties(state)).orElse(CapProperties.NULL);
                } else {
                    return state.getBlock() instanceof DynamicCapCenterBlock ? Optional.of((DynamicCapCenterBlock)state.getBlock()).map((block) -> block.getProperties(state)).orElse(CapProperties.NULL) : CapProperties.NULL;
                }
            }

        };
        if (this.isFireProof()) {
            branch.setFireSpreadSpeed(0).setFlammability(0);
        }
        return branch;
    }
}
