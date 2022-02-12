package maxhyper.dtbyg.resources;

import com.ferreusveritas.dynamictrees.api.treepacks.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.google.gson.JsonElement;
import maxhyper.dtbyg.DynamicTreesBYG;
import maxhyper.dtbyg.trees.GenOnExtraSoilSpecies;
import maxhyper.dtbyg.trees.LamentSpecies;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesBYG.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliers(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpceiesAppliers(event.getAppliers());
    }

    @SubscribeEvent
    public static void registerAppliers(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) {
        registerSpceiesAppliers(event.getAppliers());
    }

    public static void registerSpceiesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
        appliers.register("alternative_species", LamentSpecies.class, Species.class,
                LamentSpecies::setAltSpecies)
                .register("extra_soil_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
                GenOnExtraSoilSpecies::setExtraSoil)
                .register("soil_replacement_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
                GenOnExtraSoilSpecies::setSoilReplacement);
    }

//    @SubscribeEvent
//    public static void registerJsonDeserialisers(final JsonDeserialisers.RegistryEvent event) {
//        // Register cactus thickness logic kits and lock it.
//        CactusThicknessLogic.REGISTRY.postRegistryEvent();
//        CactusThicknessLogic.REGISTRY.lock();
//
//        // Register getter for cactus thickness logic.
//        JsonDeserialisers.register(CactusThicknessLogic.class,
//                new RegistryEntryDeserialiser<>(CactusThicknessLogic.REGISTRY));
//
//        // Register getter for cactus thickness enum.
//        JsonDeserialisers.register(CactusBranchBlock.CactusThickness.class,
//                new EnumDeserialiser<>(CactusBranchBlock.CactusThickness.class));
//    }

}