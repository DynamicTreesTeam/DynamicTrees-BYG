package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.genfeature.BottomFlareGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nonnull;

public class BigBottomFlareGenFeature extends BottomFlareGenFeature {

    public BigBottomFlareGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    private static final int[] curve = {0,9,8,6,4,2,1};

    @Override
    public void flareBottom(@Nonnull GenFeatureConfiguration configuration, LevelAccessor world, BlockPos rootPos, Species species) {
        Family family = species.getFamily();

        for (int i=curve.length; i>0;i--){
            int rad = TreeHelper.getRadius(world, rootPos.above(i));
            if (rad > configuration.get(MIN_RADIUS)){
                for (int j=1; j<i; j++){
                    int finalJ = j;
                    int finalI = i;
                    family.getBranch().ifPresent(branch-> branch.setRadius(
                            world, rootPos.above(finalJ), rad + curve[finalJ +(curve.length- finalI)], Direction.UP));
                }
                break;
            }
        }

    }

}
