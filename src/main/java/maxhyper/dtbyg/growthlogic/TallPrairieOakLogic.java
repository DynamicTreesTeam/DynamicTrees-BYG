package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class TallPrairieOakLogic extends PrairieOakLogic {
    public TallPrairieOakLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public Direction newDirectionSelected(Species species, Direction newDir, GrowSignal signal) {
        if (!signal.isInTrunk()) {

        }

        return newDir;
    }
}
