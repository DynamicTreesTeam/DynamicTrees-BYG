package maxhyper.dtbyg.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.family.NetherFungusFamily;
import com.ferreusveritas.dynamictrees.util.BlockBounds;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class SythianFungusFamily extends NetherFungusFamily {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(SythianFungusFamily::new);

    public SythianFungusFamily(ResourceLocation name) {
        super(name);
    }

    public BlockBounds expandLeavesBlockBounds(BlockBounds bounds) {
        return bounds.expand(2).shrink(Direction.DOWN, 1);
    }

}
