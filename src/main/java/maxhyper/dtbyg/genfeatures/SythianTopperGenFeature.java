package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SythianTopperGenFeature extends GenFeature implements IPostGenFeature {

    public static final ConfigurationProperty<LeavesProperties> LEAVES_PROPERTIES = ConfigurationProperty.property("leaves_properties", LeavesProperties.class);

    public SythianTopperGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(LEAVES_PROPERTIES);
    }

    @Override
    public ConfiguredGenFeature<GenFeature> createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(LEAVES_PROPERTIES, LeavesProperties.NULL_PROPERTIES);
    }

    @Override
    public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
        if (endPoints.isEmpty()) {
            return false;
        }

        // Manually place the highest few blocks of the conifer since the leafCluster voxmap won't handle it
        final BlockPos highest = Collections.max(endPoints, Comparator.comparingInt(Vector3i::getY));
        // Fetch leaves properties property set or the default for the Species.
        final LeavesProperties leavesProperties = configuredGenFeature.get(LEAVES_PROPERTIES)
                .elseIfInvalid(species.getLeavesProperties());

        world.setBlock(highest.above(1), leavesProperties.getDynamicLeavesState(1), 3);

        return true;
    }

}
