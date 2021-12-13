package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.BottomFlareGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.util.ResourceLocation;

public class DTBYGGenFeatures {

    public static final GenFeature BIG_BOTTOM_FLARE = new BigBottomFlareGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "big_bottom_flare"));
    public static final GenFeature EXTRA_BOTTOM_FLARE = new ExtraBottomFlareGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "extra_bottom_flare"));
    public static final GenFeature MANGROVELINGS = new MangrovelingsGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mangrovelings"));
    public static final GenFeature BRANCH_SPROUTS = new BranchSproutsGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "branch_sprouts"));

    public static void register(final IRegistry<GenFeature> registry) {
        registry.registerAll(BIG_BOTTOM_FLARE, EXTRA_BOTTOM_FLARE, MANGROVELINGS, BRANCH_SPROUTS);
    }

}
