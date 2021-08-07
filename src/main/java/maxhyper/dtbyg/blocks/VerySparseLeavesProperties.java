package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import maxhyper.dtbyg.cells.DTBYGCellKits;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class VerySparseLeavesProperties extends LeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(VerySparseLeavesProperties::new);

    public VerySparseLeavesProperties(ResourceLocation registryName) {
        super(registryName);
        setCellKit(DTBYGCellKits.SPARSE);
    }

    private float leafChance = 0.5f;

    public void setLeafChance (float leafChance){
        this.leafChance = leafChance;
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(final AbstractBlock.Properties properties) {
        return new DynamicLeavesBlock(this, properties){
            public int getHydrationLevelFromNeighbors(IWorld world, BlockPos pos, LeavesProperties leavesProperties) {
                int hash = CoordUtils.coordHashCode(pos, 2) % 1000;
                float rand = hash / 1000f;
                if (rand >= leafChance) return 0;
                return super.getHydrationLevelFromNeighbors(world, pos, leavesProperties);
            }
        };
    }
}
