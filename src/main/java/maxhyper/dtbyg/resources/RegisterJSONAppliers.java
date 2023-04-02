package maxhyper.dtbyg.resources;

import com.ferreusveritas.dynamictrees.api.applier.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import maxhyper.dtbyg.DynamicTreesBYG;
import maxhyper.dtbyg.trees.GenOnExtraSoilSpecies;
import maxhyper.dtbyg.trees.LamentSpecies;
import maxhyper.dtbyg.trees.NightshadeFamily;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesBYG.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    @SubscribeEvent
    public static void registerAppliersFamily(final ApplierRegistryEvent.Reload<Family, JsonElement> event) {
        registerFamilyAppliers(event.getAppliers());
    }

    public static void registerSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
        appliers.register("alternative_species", LamentSpecies.class, Species.class,
                LamentSpecies::setAltSpecies)
                .register("extra_soil_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
                GenOnExtraSoilSpecies::setExtraSoil)
                .register("soil_replacement_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
                GenOnExtraSoilSpecies::setSoilReplacement);
    }

    public static void registerFamilyAppliers(PropertyAppliers<Family, JsonElement> appliers) {
        appliers.register("primitive_imbued_log", NightshadeFamily.class, Block.class,
                NightshadeFamily::setPrimitiveImbuedLog);
    }

    @SubscribeEvent public static void registerAppliersSpecies(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) { registerSpeciesAppliers(event.getAppliers()); }
    @SubscribeEvent public static void registerAppliersFamily(final ApplierRegistryEvent.GatherData<Family, JsonElement> event) { registerFamilyAppliers(event.getAppliers()); }

}