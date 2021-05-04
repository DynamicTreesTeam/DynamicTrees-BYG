package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.util.ResourceLocation;

public class DTBYGGrowthLogicKits {

    public static final GrowthLogicKit POPLAR = new PoplarLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "poplar"));

    public static void register(final IRegistry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR);
    }

}
