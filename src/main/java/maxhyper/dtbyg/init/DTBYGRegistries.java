package maxhyper.dtbyg.init;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SpreadableRootyBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import com.ferreusveritas.dynamictrees.trees.Species;
import corgiaoc.byg.core.BYGBlocks;
import maxhyper.dtbyg.DynamicTreesBYG;
import maxhyper.dtbyg.blocks.BYGTintedRootyBlock;
import maxhyper.dtbyg.cells.DTBYGCellKits;
import maxhyper.dtbyg.growthlogic.DTBYGGrowthLogicKits;
import maxhyper.dtbyg.trees.PoplarSpecies;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTBYGRegistries {

    public static final String END_SAND_LIKE = "end_sand_like";
    public static final String NYLIUM_SOIL_LIKE = "nylium_soil_like";
    public static final String NYLIUM_SAND_LIKE = "nylium_sand_like";
    public static final String SCULK_LIKE = "sculk_like";

    public static LinkedList<RootyBlock> rootyBlocks;

    public static void setupBlocks() {
        setUpSoils();
        setupConnectables();
    }

    private static void setUpSoils() {
        DirtHelper.createNewAdjective(END_SAND_LIKE);
        DirtHelper.createNewAdjective(NYLIUM_SOIL_LIKE);
        DirtHelper.createNewAdjective(NYLIUM_SAND_LIKE);
        DirtHelper.createNewAdjective(SCULK_LIKE);

        DirtHelper.registerSoil(BYGBlocks.PODZOL_DACITE, DirtHelper.DIRT_LIKE);
        DirtHelper.registerSoil(BYGBlocks.OVERGROWN_DACITE, DirtHelper.DIRT_LIKE, new BYGTintedRootyBlock(BYGBlocks.OVERGROWN_DACITE));
        DirtHelper.registerSoil(BYGBlocks.OVERGROWN_STONE, DirtHelper.DIRT_LIKE, new BYGTintedRootyBlock(BYGBlocks.OVERGROWN_STONE));
        DirtHelper.registerSoil(BYGBlocks.PEAT, DirtHelper.DIRT_LIKE);
        DirtHelper.registerSoil(BYGBlocks.MEADOW_GRASSBLOCK, DirtHelper.DIRT_LIKE, new BYGTintedRootyBlock(BYGBlocks.MEADOW_GRASSBLOCK));
        DirtHelper.registerSoil(BYGBlocks.MEADOW_DIRT, DirtHelper.DIRT_LIKE, new SpreadableRootyBlock(BYGBlocks.MEADOW_DIRT, 9, BYGBlocks.MEADOW_GRASSBLOCK));
        DirtHelper.registerSoil(BYGBlocks.GLOWCELIUM, DirtHelper.DIRT_LIKE, new BYGTintedRootyBlock(BYGBlocks.GLOWCELIUM));
        DirtHelper.registerSoil(BYGBlocks.OVERGROWN_NETHERRACK, DirtHelper.DIRT_LIKE, new BYGTintedRootyBlock(BYGBlocks.OVERGROWN_NETHERRACK));
        DirtHelper.registerSoil(BYGBlocks.ETHER_PHYLIUM, DirtHelper.DIRT_LIKE);
        DirtHelper.registerSoil(BYGBlocks.ETHER_SOIL, DirtHelper.DIRT_LIKE);
        DirtHelper.registerSoil(BYGBlocks.PEAT, DirtHelper.MUD_LIKE);
        DirtHelper.registerSoil(BYGBlocks.MUD_BLOCK, DirtHelper.MUD_LIKE);
        DirtHelper.registerSoil(BYGBlocks.BLACK_SAND, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.WHITE_SAND, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.BLUE_SAND, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.PURPLE_SAND, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.PINK_SAND, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.QUARTZITE_SAND, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.END_SAND, DirtHelper.SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.END_SAND, END_SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.GLOWCELIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.SYTHIAN_NYLIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.EMBUR_NYLIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.OVERGROWN_CRIMSON_BLACKSTONE, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.MYCELIUM_NETHERRACK, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.IVIS_PHYLIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.IMPARIUS_PHYLIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.ETHER_PHYLIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.BULBIS_PHYCELIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.NIGHTSHADE_PHYLIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.SHULKREN_PHYLIUM, DirtHelper.FUNGUS_LIKE);
        DirtHelper.registerSoil(BYGBlocks.NYLIUM_SOUL_SAND, NYLIUM_SAND_LIKE);
        DirtHelper.registerSoil(BYGBlocks.NYLIUM_SOUL_SOIL, NYLIUM_SOIL_LIKE);
        DirtHelper.registerSoil(BYGBlocks.SYTHIAN_NYLIUM, DirtHelper.NETHER_LIKE);
        DirtHelper.registerSoil(BYGBlocks.EMBUR_NYLIUM, DirtHelper.NETHER_LIKE);
        DirtHelper.registerSoil(BYGBlocks.NYLIUM_SOUL_SAND, DirtHelper.NETHER_LIKE);
        DirtHelper.registerSoil(BYGBlocks.NYLIUM_SOUL_SOIL, DirtHelper.NETHER_LIKE);
        DirtHelper.registerSoil(BYGBlocks.OVERGROWN_NETHERRACK, DirtHelper.NETHER_LIKE);
        DirtHelper.registerSoil(BYGBlocks.MYCELIUM_NETHERRACK, DirtHelper.NETHER_LIKE);
        DirtHelper.registerSoil(BYGBlocks.MOSSY_NETHERRACK, DirtHelper.NETHER_LIKE, new BYGTintedRootyBlock(BYGBlocks.MOSSY_NETHERRACK));
        DirtHelper.registerSoil(BYGBlocks.BLUE_NETHERRACK, DirtHelper.NETHER_LIKE, new SpreadableRootyBlock(BYGBlocks.BLUE_NETHERRACK, Items.BONE_MEAL, BYGBlocks.EMBUR_NYLIUM));
        DirtHelper.registerSoil(BYGBlocks.SYTHIAN_NYLIUM, DirtHelper.NETHER_SOIL_LIKE);
        DirtHelper.registerSoil(BYGBlocks.EMBUR_NYLIUM, DirtHelper.NETHER_SOIL_LIKE);
        DirtHelper.registerSoil(BYGBlocks.OVERGROWN_CRIMSON_BLACKSTONE, DirtHelper.NETHER_SOIL_LIKE);
        DirtHelper.registerSoil(BYGBlocks.IVIS_PHYLIUM, DirtHelper.END_LIKE);
        DirtHelper.registerSoil(BYGBlocks.IMPARIUS_PHYLIUM, DirtHelper.END_LIKE);
        DirtHelper.registerSoil(BYGBlocks.BULBIS_PHYCELIUM, DirtHelper.END_LIKE);
        DirtHelper.registerSoil(BYGBlocks.NIGHTSHADE_PHYLIUM, DirtHelper.END_LIKE);
        DirtHelper.registerSoil(BYGBlocks.SHULKREN_PHYLIUM, DirtHelper.END_LIKE);
        DirtHelper.registerSoil(BYGBlocks.VERMILION_SCULK, SCULK_LIKE);

        RootyBlock rootyNetherrack = RootyBlockHelper.getRootyBlock(Blocks.NETHERRACK);
        if (rootyNetherrack instanceof SpreadableRootyBlock){
            SpreadableRootyBlock rootNeth = (SpreadableRootyBlock) rootyNetherrack;
            rootNeth.addSpreadableBlock(BYGBlocks.SYTHIAN_NYLIUM);
            rootNeth.addSpreadableBlock(BYGBlocks.OVERGROWN_NETHERRACK);
            rootNeth.addSpreadableBlock(BYGBlocks.MYCELIUM_NETHERRACK);
        }

        rootyBlocks = RootyBlockHelper.generateListForRegistry(true, DynamicTreesBYG.MOD_ID);
    }

    private static void setupConnectables() {

    }

    @SubscribeEvent
    public static void onCellKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<CellKit> event) {
        DTBYGCellKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onGrowthLogicKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GrowthLogicKit> event) {
        DTBYGGrowthLogicKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        event.registerType(DynamicTreesBYG.resLoc("poplar"), PoplarSpecies.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        setupBlocks();

        for (RootyBlock rooty : rootyBlocks)
            event.getRegistry().register(rooty);
    }

}
