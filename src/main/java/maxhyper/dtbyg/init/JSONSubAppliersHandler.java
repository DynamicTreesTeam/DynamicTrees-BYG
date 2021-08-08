package maxhyper.dtbyg.init;

import com.ferreusveritas.dynamictrees.api.treepacks.JsonApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import maxhyper.dtbyg.DynamicTreesBYG;
import maxhyper.dtbyg.blocks.ScruffyLeavesProperties;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesBYG.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JSONSubAppliersHandler {
    /**
     * This class handles custom appliers for special soil blocks. Addons that want soils with custom properties can
     * copy this whole class to do so.
     */
    @SubscribeEvent
    public static void registerAppliers(final JsonApplierRegistryEvent<LeavesProperties> event) {
        // Add to reload appliers only.
        if (!event.isReloadApplier())
            return;

        event.getApplierList().register("scruffy_leaf_chance", ScruffyLeavesProperties.class, Float.class, ScruffyLeavesProperties::setLeafChance)
                .register("scruffy_max_hydro", ScruffyLeavesProperties.class, Integer.class, ScruffyLeavesProperties::setMaxHydro);
    }

}
