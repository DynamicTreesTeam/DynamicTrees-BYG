package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.BottomFlareGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.util.ResourceLocation;

public class DTBYGGenFeatures {

    public static final GenFeature BIG_BOTTOM_FLARE = new BigBottomFlareGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "big_bottom_flare"));

    public static void register(final IRegistry<GenFeature> registry) {
        registry.registerAll(BIG_BOTTOM_FLARE);
    }

}
