package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import net.minecraft.resources.ResourceLocation;

public class EmburGelCapProperties extends CapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(EmburGelCapProperties::new);

    public EmburGelCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    protected String getBlockRegistryNameSuffix() {
        return "_gel";
    }

    protected String getCenterBlockRegistryNameSuffix() {
        return "_gel_center";
    }

}
