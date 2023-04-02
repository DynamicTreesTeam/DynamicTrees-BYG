package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class NightshadeFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(NightshadeFamily::new);

    protected Supplier<BranchBlock> altBranchBlock;

    public NightshadeFamily(ResourceLocation name) {
        super(name);
    }

    @Override
    public void setupBlocks() {
        super.setupBlocks();

        this.altBranchBlock = setupBranch(createBranch(getBranchName("imbued_")), false);
    }

    public Family setPrimitiveImbuedLog(Block primitiveLog) {
        altBranchBlock.get().setPrimitiveLogDrops(new ItemStack(primitiveLog));
        return this;
    }

}
