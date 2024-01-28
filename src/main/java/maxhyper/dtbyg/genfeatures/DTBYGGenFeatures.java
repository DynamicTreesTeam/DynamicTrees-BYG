package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeature.BiomePredicateGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.resources.ResourceLocation;

public class DTBYGGenFeatures {

    public static final GenFeature BIG_BOTTOM_FLARE = new BigBottomFlareGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "big_bottom_flare"));
    public static final GenFeature EXTRA_BOTTOM_FLARE = new ExtraBottomFlareGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "extra_bottom_flare"));
    //public static final GenFeature MANGROVELINGS = new MangrovelingsGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mangrovelings"));
    public static final GenFeature BRANCH_SPROUTS = new BranchSproutsGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "branch_sprouts"));
    //public static final GenFeature HUGE_MUSHROOMS = new BYGHugeMushroomGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "huge_mushrooms"));
    public static final GenFeature BIOME_PREDICATE_2 = new BiomePredicateGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "biome_predicate"));
    public static final GenFeature SYTHIAN_TOPPER = new SythianTopperGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "sythian_topper"));
    public static final GenFeature ALTERNATIVE_BRANCH = new AlternativeBranchGenFeature(new ResourceLocation(DynamicTreesBYG.MOD_ID, "alt_branch"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(BIG_BOTTOM_FLARE, EXTRA_BOTTOM_FLARE,
                //MANGROVELINGS,
                BRANCH_SPROUTS,
                // HUGE_MUSHROOMS,
                BIOME_PREDICATE_2,
                SYTHIAN_TOPPER, ALTERNATIVE_BRANCH);
    }

}
