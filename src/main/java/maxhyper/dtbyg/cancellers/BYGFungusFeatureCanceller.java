package maxhyper.dtbyg.cancellers;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.worldgen.cancellers.FungusFeatureCanceller;
import corgiaoc.byg.common.world.feature.config.BYGMushroomConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.*;

import java.util.List;

public class BYGFungusFeatureCanceller<T extends IFeatureConfig> extends FungusFeatureCanceller<T> {

    public BYGFungusFeatureCanceller(ResourceLocation registryName, Class<T> fungusFeatureConfigClass) {
        super(registryName, fungusFeatureConfigClass);
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.FeatureCancellations featureCancellations) {
        if (configuredFeature.config instanceof DecoratedFeatureConfig){
            final ConfiguredFeature<?, ?> nextConfiguredFeature = ((DecoratedFeatureConfig) configuredFeature.config).feature.get();
            if (nextConfiguredFeature.config instanceof MultipleRandomFeatureConfig){
                List<ConfiguredRandomFeatureList> featureList = ((MultipleRandomFeatureConfig)nextConfiguredFeature.config).features;
                for (ConfiguredRandomFeatureList listFeature : featureList){
                    ConfiguredFeature<?,?> confFeat = listFeature.feature.get();
                    if (confFeat.config instanceof BYGMushroomConfig)
                        return true;
                }
            }
        }
        return false;
    }

}
