package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.resources.ResourceLocation;

public class DTBYGGrowthLogicKits {

    public static final GrowthLogicKit POPLAR = new PoplarLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "poplar"));
    public static final GrowthLogicKit MAPLE = new MapleLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "maple"));
    public static final GrowthLogicKit ASPEN = new AspenLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "aspen"));
    public static final GrowthLogicKit TAPERED = new TaperedOakLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "tapered"));
    public static final GrowthLogicKit DIAGONAL_PALM = new DiagonalPalmLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "diagonal_palm"));
    public static final GrowthLogicKit ZELKOVA = new ZelkovaLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "zelkova"));
    public static final GrowthLogicKit THIN_CONIFER = new PineLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "thin_conifer"));
    public static final GrowthLogicKit MEGA_PINE = new MegaPineLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mega_pine"));
    public static final GrowthLogicKit EBONY = new EbonyLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "ebony"));
    public static final GrowthLogicKit REDWOOD = new RedwoodLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "redwood"));
    public static final GrowthLogicKit SMALL_REDWOOD = new SmallRedwoodLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "small_redwood"));
    public static final GrowthLogicKit BAOBAB = new BaobabLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "baobab"));
    public static final GrowthLogicKit VARIATE_HEIGHT = new VariateHeightLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "variate_height"));
    public static final GrowthLogicKit SYTHIAN_FUNGUS = new SythianLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "sythian_fungus"));
    public static final GrowthLogicKit CYPRESS = new CypressLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "cypress"));
    public static final GrowthLogicKit MANGROVE = new MangroveLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mangrove"));
    public static final GrowthLogicKit TAPERED_WITHERED = new TaperedWitheredOakLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "tapered_withered"));
    public static final GrowthLogicKit ANCIENT_LOGIC = new AncientLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "ancient"));
    public static final GrowthLogicKit MEGA_RAINBOW_EUCALYPTUS = new MegaRainbowEucalyptusLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mega_rainbow_eucalyptus"));
    public static final GrowthLogicKit WILLOW = new WillowLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "willow"));
    public static final GrowthLogicKit ARAUCARIA = new AraucariaLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "araucaria"));
    public static final GrowthLogicKit TWISTING = new TwistingTreeLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "twisting"));
    public static final GrowthLogicKit ETHER = new EtherTreeLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "ether"));
    public static final GrowthLogicKit ENCHANTED = new EnchantedTreeLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "enchanted"));

    public static void register(final Registry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, MAPLE, ASPEN, TAPERED, DIAGONAL_PALM,
                ZELKOVA, THIN_CONIFER, MEGA_PINE, EBONY, REDWOOD, SMALL_REDWOOD,
                BAOBAB, VARIATE_HEIGHT, SYTHIAN_FUNGUS, CYPRESS, MANGROVE, TAPERED_WITHERED,
                ANCIENT_LOGIC, MEGA_RAINBOW_EUCALYPTUS, WILLOW, ARAUCARIA, TWISTING, ETHER, ENCHANTED);
    }

}
