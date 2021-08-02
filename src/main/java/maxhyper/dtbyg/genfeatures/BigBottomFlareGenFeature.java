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

public class BigBottomFlareGenFeature extends BottomFlareGenFeature {

    public BigBottomFlareGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    private static final int[] curve = {0,9,8,6,4,2,1};

    @Override
    public void flareBottom(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species) {
        Family family = species.getFamily();

        for (int i=curve.length; i>0;i--){
            int rad = TreeHelper.getRadius(world, rootPos.above(i));
            if (rad > configuredGenFeature.get(MIN_RADIUS)){
                for (int j=1; j<i; j++){
                    family.getBranch().setRadius(
                            world, rootPos.above(j), rad + curve[j+(curve.length-i)], Direction.UP);
                }
                break;
            }
        }

    }

}
