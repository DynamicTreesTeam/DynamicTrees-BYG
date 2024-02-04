package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeature.BiomePredicateGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.resources.ResourceLocation;

public class DTBYGGenFeatures {

    public static final GenFeature BIG_BOTTOM_FLARE = new BigBottomFlareGenFeature(DynamicTreesBYG.location("big_bottom_flare"));
    public static final GenFeature EXTRA_BOTTOM_FLARE = new ExtraBottomFlareGenFeature(DynamicTreesBYG.location("extra_bottom_flare"));
    //public static final GenFeature MANGROVELINGS = new MangrovelingsGenFeature(DynamicTreesBYG.location("mangrovelings"));
    public static final GenFeature BRANCH_SPROUTS = new BranchSproutsGenFeature(DynamicTreesBYG.location("branch_sprouts"));
    //public static final GenFeature HUGE_MUSHROOMS = new BYGHugeMushroomGenFeature(DynamicTreesBYG.location("huge_mushrooms"));
    public static final GenFeature BIOME_PREDICATE_2 = new BiomePredicateGenFeature(DynamicTreesBYG.location("biome_predicate"));
    public static final GenFeature SYTHIAN_TOPPER = new SythianTopperGenFeature(DynamicTreesBYG.location("sythian_topper"));
    public static final GenFeature ALTERNATIVE_BRANCH = new AlternativeBranchGenFeature(DynamicTreesBYG.location("alt_branch"));
    public static final GenFeature MUSHROOM_VINES = new MushroomVinesGenFeature(DynamicTreesBYG.location("mushroom_vines"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(BIG_BOTTOM_FLARE, EXTRA_BOTTOM_FLARE,
                //MANGROVELINGS,
                BRANCH_SPROUTS,
                // HUGE_MUSHROOMS,
                BIOME_PREDICATE_2,
                SYTHIAN_TOPPER, ALTERNATIVE_BRANCH, MUSHROOM_VINES);
    }

}
