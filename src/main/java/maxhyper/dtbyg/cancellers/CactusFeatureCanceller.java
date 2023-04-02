package maxhyper.dtbyg.cancellers;

import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Random;
import java.util.Set;

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
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, Set<String> namespaces) {
//        ResourceLocation featureResLoc = configuredFeature.feature().getRegistryName();
//        if (featureResLoc == null)
//            return false;
//
//        FeatureConfiguration featureConfig = configuredFeature.config();
//
//        if (!(featureConfig instanceof DecoratedFeatureConfigu)){
//            if (featureConfig instanceof MultipleRandomFeatureConfig) {
//                MultipleRandomFeatureConfig config = (MultipleRandomFeatureConfig)featureConfig;
//                List<ConfiguredFeature<?, ?>> list = config.features.stream().map((a) -> a.feature.get()).distinct().collect(Collectors.toCollection(LinkedList::new));
//                list.add(config.defaultFeature.get());
//                for (ConfiguredFeature<?,?> conFeat : list){
//                    if (conFeat.config instanceof BlockClusterFeatureConfig){
//                        final ResourceLocation featureResLoc = conFeat.feature.getRegistryName();
//                        final BlockClusterFeatureConfig blockClusterFeatureConfig = ((BlockClusterFeatureConfig) conFeat.config);
//                        final BlockStateProvider stateProvider = blockClusterFeatureConfig.stateProvider;
//
//                        if (!(stateProvider instanceof SimpleBlockStateProvider))
//                            return false;
//
//                        // SimpleBlockStateProvider does not use random or BlockPos in getBlockState, so giving null is safe.
//                        return this.cactusBlockClass.isInstance(stateProvider.getState(PLACEHOLDER_RAND, BlockPos.ZERO).getBlock())
//                                && featureResLoc != null && featureCancellations.shouldCancelNamespace(featureResLoc.getNamespace());
//                    }
//                }
//            } else return false;
//        }
        return false;
    }

}
