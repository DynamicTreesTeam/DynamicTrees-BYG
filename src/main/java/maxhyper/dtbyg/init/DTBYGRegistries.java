package maxhyper.dtbyg.init;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilHelper;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SpreadableSoilProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.WaterSoilProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.systems.dropcreators.FruitDropCreator;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.families.NetherFungusFamily;
import com.ferreusveritas.dynamictrees.util.ShapeUtils;
import corgiaoc.byg.core.BYGBlocks;
import maxhyper.dtbyg.DynamicTreesBYG;
import maxhyper.dtbyg.blocks.BYGTintedSoilProperties;
import maxhyper.dtbyg.blocks.EtherBulbsFruitBlock;
import maxhyper.dtbyg.cells.DTBYGCellKits;
import maxhyper.dtbyg.growthlogic.DTBYGGrowthLogicKits;
import maxhyper.dtbyg.trees.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTBYGRegistries {

    public static FruitBlock ETHER_BULBS_FRUIT = new EtherBulbsFruitBlock()
            .setShape(1, ShapeUtils.createFruitShape(2,3,0))
            .setShape(2, ShapeUtils.createFruitShape(2.5f,4,2))
            .setShape(3, ShapeUtils.createFruitShape(3.5f,4,3))
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static final String END_SAND_LIKE = "end_sand_like";
    public static final String NYLIUM_SOUL_LIKE = "nylium_soul_like";
    public static final String SCULK_LIKE = "sculk_like";

    public static void setup() {
        RegistryHandler.addBlock(DynamicTreesBYG.resLoc("ether_bulbs_fruit"), ETHER_BULBS_FRUIT);
    }

    public static void setupBlocks() {
        setUpSoils();
        setupConnectables();
    }

    private static void setUpSoils() {
        SoilProperties netherrackProperties = SoilHelper.getProperties(Blocks.NETHERRACK);
        if (netherrackProperties instanceof SpreadableSoilProperties)
            ((SpreadableSoilProperties) netherrackProperties).addSpreadableSoils(
                    BYGBlocks.SYTHIAN_NYLIUM, BYGBlocks.OVERGROWN_NETHERRACK, BYGBlocks.MYCELIUM_NETHERRACK);
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
        event.registerType(DynamicTreesBYG.resLoc("ether"), EtherSpecies.TYPE);
        event.registerType(DynamicTreesBYG.resLoc("twiglet"), TwigletSpecies.TYPE);
    }
    
    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
        event.registerType(DynamicTreesBYG.resLoc("diagonal_palm"), DiagonalPalmFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
    }

    @SubscribeEvent
    public static void registerSoilPropertiesTypes (final TypeRegistryEvent<SoilProperties> event) {
        event.registerType(DynamicTreesBYG.resLoc( "offset_tint"), BYGTintedSoilProperties.TYPE);
    }

    @SubscribeEvent
    public static void onItemsRegistry (final RegistryEvent.Register<Item> event) {
        Item etherBulbs = ForgeRegistries.ITEMS.getValue(new ResourceLocation("byg","ether_bulbs"));
        ETHER_BULBS_FRUIT.setDroppedItem(new ItemStack(etherBulbs));

    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        setupBlocks();

    }

}
