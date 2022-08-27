package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SythianTopperGenFeature extends GenFeature {

    public static final ConfigurationProperty<LeavesProperties> LEAVES_PROPERTIES = ConfigurationProperty.property("leaves_properties", LeavesProperties.class);

    public SythianTopperGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(LEAVES_PROPERTIES);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(LEAVES_PROPERTIES, LeavesProperties.NULL);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        List<BlockPos> endPoints = context.endPoints();
        if (endPoints.isEmpty()) {
            return false;
        }

        // Manually place the highest few blocks of the conifer since the leafCluster voxmap won't handle it
        final BlockPos highest = Collections.max(endPoints, Comparator.comparingInt(Vector3i::getY));
        // Fetch leaves properties property set or the default for the Species.
        final LeavesProperties leavesProperties = configuration.get(LEAVES_PROPERTIES)
                .elseIfInvalid(context.species().getLeavesProperties());

        context.world().setBlock(highest.above(1), leavesProperties.getDynamicLeavesState(1), 3);

        return true;
    }

}
