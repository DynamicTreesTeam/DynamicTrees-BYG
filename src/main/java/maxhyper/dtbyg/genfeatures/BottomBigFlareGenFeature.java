package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.genfeatures.BottomFlareGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BottomBigFlareGenFeature extends BottomFlareGenFeature {

    public BottomBigFlareGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public void flareBottom(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species) {
        Family family = species.getFamily();

        //Put a big fat flare for trees like ebony
        int radius8 = TreeHelper.getRadius(world, rootPos.above(8));

        if(radius8 > configuredGenFeature.get(MIN_RADIUS)) {
            family.getBranch().setRadius(world, rootPos.above(7), radius8 + 1, Direction.UP);
            family.getBranch().setRadius(world, rootPos.above(6), radius8 + 2, Direction.UP);
            family.getBranch().setRadius(world, rootPos.above(5), radius8 + 4, Direction.UP);
            family.getBranch().setRadius(world, rootPos.above(4), radius8 + 6, Direction.UP);
            family.getBranch().setRadius(world, rootPos.above(3), radius8 + 8, Direction.UP);
            family.getBranch().setRadius(world, rootPos.above(2), radius8 + 9, Direction.UP);
            family.getBranch().setRadius(world, rootPos.above(1), radius8 + 10, Direction.UP);
        }
    }

}
