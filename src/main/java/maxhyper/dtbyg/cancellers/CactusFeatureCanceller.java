package maxhyper.dtbyg.cancellers;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.block.Block;
import net.minecraft.block.CactusBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Max Hyper
 */
public class CactusFeatureCanceller<T extends Block> extends FeatureCanceller {

    private static final Random PLACEHOLDER_RAND = new Random();

    private final Class<T> cactusBlockClass;

    public CactusFeatureCanceller(final ResourceLocation registryName, Class<T> cactusBlockClass) {
        super(registryName);
        this.cactusBlockClass = cactusBlockClass;
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.FeatureCancellations featureCancellations) {
        IFeatureConfig featureConfig = configuredFeature.config;

        if (!(featureConfig instanceof DecoratedFeatureConfig))
            return false;

        featureConfig = ((DecoratedFeatureConfig) featureConfig).feature.get().config;

        if (!(featureConfig instanceof DecoratedFeatureConfig)){
            if (featureConfig instanceof MultipleRandomFeatureConfig) {
                MultipleRandomFeatureConfig config = (MultipleRandomFeatureConfig)featureConfig;
                List<ConfiguredFeature<?, ?>> list = config.features.stream().map((a) -> a.feature.get()).distinct().collect(Collectors.toCollection(LinkedList::new));
                list.add(config.defaultFeature.get());
                for (ConfiguredFeature<?,?> conFeat : list){
                    if (conFeat.config instanceof BlockClusterFeatureConfig){
                        final ResourceLocation featureResLoc = conFeat.feature.getRegistryName();
                        final BlockClusterFeatureConfig blockClusterFeatureConfig = ((BlockClusterFeatureConfig) conFeat.config);
                        final BlockStateProvider stateProvider = blockClusterFeatureConfig.stateProvider;

                        if (!(stateProvider instanceof SimpleBlockStateProvider))
                            return false;

                        // SimpleBlockStateProvider does not use random or BlockPos in getBlockState, so giving null is safe.
                        return this.cactusBlockClass.isInstance(stateProvider.getState(PLACEHOLDER_RAND, BlockPos.ZERO).getBlock())
                                && featureResLoc != null && featureCancellations.shouldCancelNamespace(featureResLoc.getNamespace());
                    }
                }
            } else return false;
        }
        return false;
    }

}
