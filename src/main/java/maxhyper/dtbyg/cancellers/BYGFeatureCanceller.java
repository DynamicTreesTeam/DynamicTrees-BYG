package maxhyper.dtbyg.cancellers;

import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Set;

public class BYGFeatureCanceller<T extends FeatureConfiguration> extends FeatureCanceller {

    private final Class<T> featureConfigClass;

    public BYGFeatureCanceller(ResourceLocation registryName, Class<T> featureConfigClass) {
        super(registryName);
        this.featureConfigClass = featureConfigClass;
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, Set<String> namespaces) {
        return false;
    }

//    @Override
//    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.FeatureCancellations featureCancellations) {
//        if (configuredFeature.config instanceof DecoratedFeatureConfig){
//
//            final ConfiguredFeature<?, ?> nextConfiguredFeature = ((DecoratedFeatureConfig) configuredFeature.config).feature.get();
//            if (nextConfiguredFeature.config instanceof MultipleRandomFeatureConfig){
//                return cancelMultipleRandomFeature((MultipleRandomFeatureConfig)nextConfiguredFeature.config);
//            } else if (nextConfiguredFeature.config instanceof DecoratedFeatureConfig){
//
//                ConfiguredFeature<?,?> feature1 = ((DecoratedFeatureConfig)nextConfiguredFeature.config).feature.get();
//                if (feature1.config instanceof MultipleRandomFeatureConfig) {
//                    return cancelMultipleRandomFeature((MultipleRandomFeatureConfig) feature1.config);
//                } else
//                    return featureConfigClass.isInstance(feature1.config);
//
//            } else
//                return featureConfigClass.isInstance(nextConfiguredFeature.config);
//
//        }
//        return false;
//    }
//
//    protected boolean cancelMultipleRandomFeature(MultipleRandomFeatureConfig config){
//        List<ConfiguredRandomFeatureList> featureList = config.features;
//        for (ConfiguredRandomFeatureList listFeature : featureList){
//            ConfiguredFeature<?,?> confFeat = listFeature.feature.get();
//            if (featureConfigClass.isInstance(confFeat.config))
//                return true;
//        }
//        return false;
//    }

}
