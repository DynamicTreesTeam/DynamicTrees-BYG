package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.trees.Family;
import corgiaoc.byg.core.BYGBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NightshadeFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(NightshadeFamily::new);

    protected BranchBlock altBranchBlock;

    public NightshadeFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    public void setupBlocks() {
        super.setupBlocks();

        this.altBranchBlock = setupBranch(
                createBranch(getBranchRegName("imbued_")),
                false
        );
    }

    public Family setPrimitiveImbuedLog(Block primitiveLog) {
        altBranchBlock.setPrimitiveLogDrops(new ItemStack(primitiveLog));
        return this;
    }

}
