package maxhyper.dtbyg.init;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.block.rooty.SoilHelper;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SpreadableSoilProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.BranchConnectables;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CommonVoxelShapes;
import com.ferreusveritas.dynamictrees.worldgen.featurecancellation.TreeFeatureCanceller;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import maxhyper.dtbyg.DynamicTreesBYG;
import maxhyper.dtbyg.blocks.DynamicArisianBloomBranch;
import maxhyper.dtbyg.blocks.LavaSoilProperties;
import maxhyper.dtbyg.cancellers.VegetationReplacement;
import maxhyper.dtbyg.cells.DTBYGCellKits;
import maxhyper.dtbyg.fruits.EtherBulbsFruit;
import maxhyper.dtbyg.genfeatures.DTBYGGenFeatures;
import maxhyper.dtbyg.growthlogic.DTBYGGrowthLogicKits;
import maxhyper.dtbyg.mushroomshape.BYGMushroomShapeKits;
import maxhyper.dtbyg.trees.*;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import potionstudios.byg.BYGConstants;
import potionstudios.byg.common.block.BYGBlocks;
import potionstudios.byg.common.world.feature.config.BYGMushroomConfig;
import potionstudios.byg.common.world.feature.config.BYGTreeConfig;
import potionstudios.byg.common.world.feature.config.GiantFlowerConfig;
import potionstudios.byg.common.world.feature.gen.overworld.trees.structure.TreeFromStructureNBTConfig;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTBYGRegistries {

    public static final VoxelShape MUSHROOM_STEM_LONG = Block.box(7D, 0D, 7D, 9D, 10D, 9D);
    public static final VoxelShape TALL_MUSHROOM_CAP_FLAT = Block.box(4.0D, 8.0D, 4.0D, 12.0D, 11.0D, 12.0D);
    public static final VoxelShape SMALL_MUSHROOM_CAP_FLAT = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 7.0D, 11.0D);
    public static final VoxelShape MUSHROOM_CAP_SHORT_ROUND = Block.box(5.0D, 3.0D, 5.0D, 11.0D, 7.0D, 11.0D);
    public static final VoxelShape SOUL_SHROOM_CAP = Block.box(5.5D, 3.0D, 5.5D, 10.5D, 10.0D, 10.5D);
    public static final VoxelShape SYTHIAN_CAP_A = Block.box(5D, 3D, 5D, 11D, 5D, 11D);
    public static final VoxelShape SYTHIAN_CAP_B = Block.box(4D, 6D, 4D, 12D, 8D, 12D);
    public static final VoxelShape SYTHIAN_CAP_C = Block.box(5D, 9D, 5D, 11D, 11D, 11D);
    public static final VoxelShape SHULKREN_CAP_A = Block.box(4D, 3D, 4D, 12D, 6D, 12D);
    public static final VoxelShape SHULKREN_CAP_B = Block.box(5D, 6D, 5D, 11D, 9D, 11D);
    public static final VoxelShape SHULKREN_CAP_C = Block.box(6D, 9D, 6D, 10D, 11D, 10D);

    public static final VoxelShape TALL_FLAT_MUSHROOM = Shapes.or(MUSHROOM_STEM_LONG, TALL_MUSHROOM_CAP_FLAT);
    public static final VoxelShape SMALL_FLAT_MUSHROOM = Shapes.or(CommonVoxelShapes.MUSHROOM_STEM, SMALL_MUSHROOM_CAP_FLAT);
    public static final VoxelShape SHORT_ROUND_MUSHROOM = Shapes.or(CommonVoxelShapes.MUSHROOM_STEM, MUSHROOM_CAP_SHORT_ROUND);
    public static final VoxelShape SOUL_SHROOM = Shapes.or(CommonVoxelShapes.MUSHROOM_STEM, SOUL_SHROOM_CAP);
    public static final VoxelShape SYTHIAN_MUSHROOM = Shapes.or(MUSHROOM_STEM_LONG, SYTHIAN_CAP_A, SYTHIAN_CAP_B, SYTHIAN_CAP_C);
    public static final VoxelShape SHULKREN_MUSHROOM = Shapes.or(CommonVoxelShapes.MUSHROOM_STEM, SHULKREN_CAP_A, SHULKREN_CAP_B, SHULKREN_CAP_C);


    public static Supplier<DynamicArisianBloomBranch> ARISIAN_BLOOM_BRANCH = RegistryHandler.addBlock(DynamicTreesBYG.location("arisian_bloom_branch"), ()->new DynamicArisianBloomBranch(BYGBlocks.ARISIAN_BLOOM_BRANCH.get()));
    public static Supplier<DynamicArisianBloomBranch> EMBUR_GEL_BRANCH = RegistryHandler.addBlock(DynamicTreesBYG.location("embur_gel_branch"), ()->new DynamicArisianBloomBranch(BYGBlocks.EMBUR_GEL_BRANCH.get()));
    public static Supplier<DynamicArisianBloomBranch> WITCH_HAZEL_BRANCH = RegistryHandler.addBlock(DynamicTreesBYG.location("witch_hazel_side_branch"), ()->new DynamicArisianBloomBranch(BYGBlocks.WITCH_HAZEL_BRANCH.get()));
    public static Supplier<DynamicArisianBloomBranch> IMPARIUS_MUSHROOM_BRANCH = RegistryHandler.addBlock(DynamicTreesBYG.location("imparius_mushroom_side_branch"), ()->new DynamicArisianBloomBranch(BYGBlocks.IMPARIUS_MUSHROOM_BRANCH.get()));

    public static void setup() {
        BYGConstants.ENABLE_CACTI = false;

        CommonVoxelShapes.SHAPES.put(DynamicTreesBYG.location("tall_flat_mushroom").toString(), TALL_FLAT_MUSHROOM);
        CommonVoxelShapes.SHAPES.put(DynamicTreesBYG.location("small_flat_mushroom").toString(), SMALL_FLAT_MUSHROOM);
        CommonVoxelShapes.SHAPES.put(DynamicTreesBYG.location("short_round_mushroom").toString(), SHORT_ROUND_MUSHROOM);
        CommonVoxelShapes.SHAPES.put(DynamicTreesBYG.location("soul_shroom").toString(), SOUL_SHROOM);
        CommonVoxelShapes.SHAPES.put(DynamicTreesBYG.location("sythian_mushroom").toString(), SYTHIAN_MUSHROOM);
        CommonVoxelShapes.SHAPES.put(DynamicTreesBYG.location("shulkren_mushroom").toString(), SHULKREN_MUSHROOM);

    }

    public static void setupBlocks() {
        setUpSoils();
        setupConnectables();
    }

    private static void setUpSoils() {
        SoilProperties netherrackProperties = SoilHelper.getProperties(Blocks.NETHERRACK);
        if (netherrackProperties instanceof SpreadableSoilProperties)
            ((SpreadableSoilProperties) netherrackProperties).addSpreadableSoils(
                    BYGBlocks.SYTHIAN_NYLIUM.get(), BYGBlocks.OVERGROWN_NETHERRACK.get(), BYGBlocks.MYCELIUM_NETHERRACK.get());
    }

    private static void setupConnectables() {
        BranchConnectables.makeBlockConnectable(BYGBlocks.POLLEN_BLOCK.get(), (state, world, pos, side) -> {
            if (side == Direction.DOWN) return 1;
            return 0;
        });

        BranchConnectables.makeBlockConnectable(BYGBlocks.PURPLE_SHROOMLIGHT.get(), (state, world, pos, side) -> {
            if (side == Direction.DOWN) {
                BlockState branchState = world.getBlockState(pos.relative(Direction.UP));
                BranchBlock branch = TreeHelper.getBranch(branchState);
                if (branch != null)
                    return Math.min(Math.max(branch.getRadius(branchState) - 1, 1), 8);
                else return 8;
            }
            return 0;
        });

        for (Block block : new Block[]{ARISIAN_BLOOM_BRANCH.get(), EMBUR_GEL_BRANCH.get(), WITCH_HAZEL_BRANCH.get(), IMPARIUS_MUSHROOM_BRANCH.get()}){
            BranchConnectables.makeBlockConnectable(block, (state, world, pos, side) -> {
                if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
                    return state.getValue(HorizontalDirectionalBlock.FACING) == side ? 1 : 0;
                }
                return 0;
            });
        }
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        DTBYGGenFeatures.register(event.getRegistry());
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
        event.registerType(DynamicTreesBYG.location("poplar"), PoplarSpecies.TYPE);
        event.registerType(DynamicTreesBYG.location("twiglet"), TwigletSpecies.TYPE);
        event.registerType(DynamicTreesBYG.location("generates_underwater"), GenUnderwaterSpecies.TYPE);
        event.registerType(DynamicTreesBYG.location("generates_on_extra_soil"), GenOnExtraSoilSpecies.TYPE);
        event.registerType(DynamicTreesBYG.location("mangrove"), MangroveSpecies.TYPE);
        event.registerType(DynamicTreesBYG.location("lament"), LamentSpecies.TYPE);
    }
    
    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
        event.registerType(DynamicTreesBYG.location("diagonal_palm"), DiagonalPalmFamily.TYPE);
        event.registerType(DynamicTreesBYG.location("sythian_fungus"), SythianFungusFamily.TYPE);
        event.registerType(DynamicTreesBYG.location("nightshade"), NightshadeFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerSoilPropertiesTypes (final TypeRegistryEvent<SoilProperties> event) {
        event.registerType(DynamicTreesBYG.location( "lava"), LavaSoilProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerFruitTypes(final TypeRegistryEvent<Fruit> event) {
        event.registerType(DynamicTreesBYG.location("ether_bulbs"), EtherBulbsFruit.TYPE);
    }

    @SubscribeEvent
    public static void onMushroomShapeKitRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<MushroomShapeKit> event) {
        BYGMushroomShapeKits.register(event.getRegistry());
    }

    public static final FeatureCanceller BYG_TREE_CANCELLER = new TreeFeatureCanceller<>(DynamicTreesBYG.location("tree"), BYGTreeConfig.class);
    public static final FeatureCanceller BYG_TREE_STRUCTURE_CANCELLER = new TreeFeatureCanceller<>(DynamicTreesBYG.location("tree_structure"), TreeFromStructureNBTConfig.class);
    public static final FeatureCanceller BYG_FUNGUS_CANCELLER = new TreeFeatureCanceller<>(DynamicTreesBYG.location("fungus"), BYGMushroomConfig.class);
    public static final FeatureCanceller GIANT_FLOWER_CANCELLER = new TreeFeatureCanceller<>(DynamicTreesBYG.location("giant_flower"), GiantFlowerConfig.class);

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {
        event.getRegistry().registerAll(BYG_TREE_CANCELLER);
        event.getRegistry().registerAll(BYG_TREE_STRUCTURE_CANCELLER);
        event.getRegistry().registerAll(BYG_FUNGUS_CANCELLER);
        event.getRegistry().registerAll(GIANT_FLOWER_CANCELLER);
    }

    public static void onBiomeLoading(final BiomeLoadingEvent event){
        VegetationReplacement.OnBiomeLoadingEvent(event);
    }

}
