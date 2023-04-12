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
        ResourceLocation featureResLoc = configuredFeature.feature().getRegistryName();
        if (featureResLoc == null)
            return false;

        FeatureConfiguration featureConfig = configuredFeature.config();

        if (featureConfig instanceof RandomPatchConfiguration randomPatchConfiguration) {
            PlacedFeature placedFeature = randomPatchConfiguration.feature().value();
            featureConfig = placedFeature.feature().value().config();
        }

        if (!(featureConfig instanceof BlockColumnConfiguration blockColumnConfiguration) || !namespaces.contains(featureResLoc.getNamespace())) {
            return false;
        }

        for (BlockColumnConfiguration.Layer layer : blockColumnConfiguration.layers()) {
            final BlockStateProvider stateProvider = layer.state();
            if (!(stateProvider instanceof SimpleStateProvider)) {
                continue;
            }

            // SimpleStateProvider does not use Random or BlockPos in getState, but we still provide non-null values just to be safe
            if (this.cactusBlockClass.isInstance(stateProvider.getState(PLACEHOLDER_RAND, BlockPos.ZERO).getBlock())) {
                return true;
            }
        }

        return false;
    }

}
