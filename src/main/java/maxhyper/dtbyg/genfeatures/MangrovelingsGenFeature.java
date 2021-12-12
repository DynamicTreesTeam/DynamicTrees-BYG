package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilHelper;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilProperties;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class MangrovelingsGenFeature extends GenFeature implements IPostGenFeature {

    public MangrovelingsGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
        int[] angles = new int[2];
        angles[0] = angles[1] = world.getRandom().nextInt(6);
        while(angles[0] == angles[1]) {
            angles[1] = world.getRandom().nextInt(6);
        }

        anglesLoop:
        for(int a : angles) {
            double angle = Math.toRadians(a * 60.0f);
            float distance = 3.0f + world.getRandom().nextFloat() * 2.0f;
            BlockPos offPos = rootPos.offset(new Vector3i(Math.sin(angle) * distance, 0, Math.cos(angle) * distance));

            if(safeBounds.inBounds(offPos, true)) {
                if(species.isAcceptableSoil(world, offPos, world.getBlockState(offPos))) {
                    if( !(world.isEmptyBlock(offPos.above(1)) && world.isEmptyBlock(offPos.above(2))) ) {
                        continue;
                    }
                    for(Direction hor : CoordUtils.HORIZONTALS) {
                        BlockPos offPos2 = offPos.offset(hor.getNormal());
                        if( !(world.isEmptyBlock(offPos2.above(1)) && world.isEmptyBlock(offPos2.above(2))) ) {
                            continue anglesLoop;
                        }
                    }

                    species.placeRootyDirtBlock(world, offPos, 0);
                    species.getFamily().getBranch().setRadius(world, offPos.above(1), 1, Direction.DOWN, 0);
                    if(world.getRandom().nextInt(2) == 0) {
                        world.setBlock(offPos.above(2), species.getLeavesProperties().getDynamicLeavesState(1), 3);
                    } else {
                        species.getFamily().getBranch().setRadius(world, offPos.above(2), 1, Direction.DOWN, 0);
                        if(world.isEmptyBlock(offPos.above(3))) {
                            world.setBlock(offPos.above(3), species.getLeavesProperties().getDynamicLeavesState(1), 3);
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    protected void registerProperties() {

    }
}
