package maxhyper.dtbyg.cancellers;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.worldgen.cancellers.FungusFeatureCanceller;
import corgiaoc.byg.common.world.feature.config.BYGMushroomConfig;
import corgiaoc.byg.common.world.feature.config.GiantFlowerConfig;
import corgiaoc.byg.common.world.feature.overworld.giantflowers.util.BYGAbstractGiantFlowerFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.*;

import java.util.List;

public class BYGFungusFeatureCanceller<T extends IFeatureConfig> extends FungusFeatureCanceller<T> {

    private final Class<T> fungusFeatureConfigClass;

    public BYGFungusFeatureCanceller(ResourceLocation registryName, Class<T> fungusFeatureConfigClass) {
        super(registryName, fungusFeatureConfigClass);
        this.fungusFeatureConfigClass = fungusFeatureConfigClass;
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.FeatureCancellations featureCancellations) {
        if (configuredFeature.config instanceof DecoratedFeatureConfig){
            final ConfiguredFeature<?, ?> nextConfiguredFeature = ((DecoratedFeatureConfig) configuredFeature.config).feature.get();
            if (nextConfiguredFeature.config instanceof MultipleRandomFeatureConfig){
                List<ConfiguredRandomFeatureList> featureList = ((MultipleRandomFeatureConfig)nextConfiguredFeature.config).features;
                for (ConfiguredRandomFeatureList listFeature : featureList){
                    ConfiguredFeature<?,?> confFeat = listFeature.feature.get();
                    if (fungusFeatureConfigClass.isInstance(confFeat.config))
                        return true;
                }
            }
        }
        return false;
    }

}
