package maxhyper.dtbyg.cancellers;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.google.common.collect.ImmutableList;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import potionstudios.byg.common.block.BYGBlocks;
import potionstudios.byg.common.block.BYGWoodTypes;
import potionstudios.byg.common.world.biome.BYGBiomes;
import potionstudios.byg.common.world.feature.features.end.BYGEndVegetationFeatures;
import potionstudios.byg.common.world.feature.features.nether.BYGNetherVegetationFeatures;
import potionstudios.byg.common.world.feature.features.overworld.BYGOverworldVegetationFeatures;
import potionstudios.byg.common.world.feature.placement.BYGPlacedFeaturesUtil;

public class VegetationReplacement {

    public static void replaceNyliumFungiFeatures() {
        TreeRegistry.findSpecies(DynamicTreesBYG.location("shulkren")).getSapling().ifPresent((shulkrenSapling) ->
                TreeRegistry.findSpecies(DynamicTreesBYG.location("embur")).getSapling().ifPresent((emburSapling) ->
                        TreeRegistry.findSpecies(DynamicTreesBYG.location("sythian")).getSapling().ifPresent((sythianSapling) -> {
                            replacePatchConfig(BYGEndVegetationFeatures.SHULKREN_FUNGUS.value(), shulkrenSapling, BYGBlocks.SHULKREN_FUNGUS.get());
                            replaceFeatureConfig(BYGNetherVegetationFeatures.EMBUR_BOG_VEGETATION.value(), emburSapling, BYGWoodTypes.EMBUR.growerItem().get());
                            replaceFeatureConfig(BYGNetherVegetationFeatures.SYTHIAN_VEGETATION.value(), sythianSapling, BYGWoodTypes.SYTHIAN.growerItem().get());
                        })));
    }

    @SuppressWarnings("unchecked")
    private static void replaceFeatureConfig(ConfiguredFeature<RandomFeatureConfiguration,?> configuredFeature, Block dynamicSapling, Block basicSapling) {
        replacePatchConfig((ConfiguredFeature<RandomPatchConfiguration,?>)configuredFeature.config().defaultFeature.value().feature().value(), dynamicSapling, basicSapling);
    }
    private static void replacePatchConfig(ConfiguredFeature<RandomPatchConfiguration,?> configuredFeature, Block dynamicSapling, Block basicSapling) {
        var f2 = configuredFeature.config().feature().value().feature().value();
        if (f2.config() instanceof SimpleBlockConfiguration sbc && sbc.toPlace() instanceof SimpleStateProvider ssp && ssp.state.is(basicSapling))
            ssp.state = dynamicSapling.defaultBlockState();

    }

//    private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, DynamicTreesBYG.MOD_ID);
//    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, DynamicTreesBYG.MOD_ID);
//
//    public static final BlockPredicateFilter SAND_FILTER = BlockPredicateFilter.forPredicate(BlockPredicate.matchesTag(BlockTags.SAND, BlockPos.ZERO.below()));
//    public static RegistryObject<ConfiguredFeature<?,?>> WINDSWEPT_DESERT_VEGETATION =
//            CONFIGURED_FEATURES.register("windswept_desert_vegetation_no_vanilla_cacti",
//                    () -> new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(ImmutableList.of(
//                            new WeightedPlacedFeature(BYGPlacedFeaturesUtil.createPlacedFeature(BYGOverworldVegetationFeatures.MINI_CACTI, SAND_FILTER), 0.15F),
//                            new WeightedPlacedFeature(BYGPlacedFeaturesUtil.createPlacedFeature(BYGOverworldVegetationFeatures.PRICKLY_PEAR_CACTI, SAND_FILTER), 0.15F),
//                            new WeightedPlacedFeature(BYGPlacedFeaturesUtil.createPlacedFeature(BYGOverworldVegetationFeatures.ALOE_VERA, SAND_FILTER), 0.3F)
//                    ), BYGPlacedFeaturesUtil.createPlacedFeature(BYGOverworldVegetationFeatures.GOLDEN_SPINED_CACTI, SAND_FILTER))));
//
//    public static RegistryObject<PlacedFeature> WINDSWEPT_DESERT_VEGETATION_HOLDER = PLACED_FEATURES.register("windswept_desert_vegetation_no_vanilla_cacti", ()-> new PlacedFeature( WINDSWEPT_DESERT_VEGETATION.getHolder().orElseThrow(), VegetationPlacements.worldSurfaceSquaredWithCount(8)));
//
//    public static void OnBiomeLoadingEvent (final BiomeLoadingEvent event){
//        if (BYGBiomes.WINDSWEPT_DESERT.location().equals(event.getName()) &&
//                WINDSWEPT_DESERT_VEGETATION_HOLDER.getHolder().isPresent()){
//            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WINDSWEPT_DESERT_VEGETATION_HOLDER.getHolder().get());
//        }
//    }
//
//    public static void register(IEventBus bus) {
//        CONFIGURED_FEATURES.register(bus);
//        PLACED_FEATURES.register(bus);
//    }

}
