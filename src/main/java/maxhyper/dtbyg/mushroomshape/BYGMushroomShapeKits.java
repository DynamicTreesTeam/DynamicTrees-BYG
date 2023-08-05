package maxhyper.dtbyg.mushroomshape;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictreesplus.DynamicTreesPlus;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.shapekits.BellShape;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import maxhyper.dtbyg.DynamicTreesBYG;

public class BYGMushroomShapeKits {

    public static final MushroomShapeKit LINEAR_MUSHROOM_SHAPE = new LinearShape(DynamicTreesBYG.location("linear"));

    public static void register(final Registry<MushroomShapeKit> registry) {
        registry.register(LINEAR_MUSHROOM_SHAPE);
    }

}
