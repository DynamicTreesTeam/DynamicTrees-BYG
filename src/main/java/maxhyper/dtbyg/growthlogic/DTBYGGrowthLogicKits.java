package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.util.ResourceLocation;

public class DTBYGGrowthLogicKits {

    public static final GrowthLogicKit POPLAR = new PoplarLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "poplar"));
    public static final GrowthLogicKit PRAIRIE = new PrairieOakLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "prairie"));
    public static final GrowthLogicKit MEGA_PRAIRIE = new MegaPrairieOakLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mega_prairie"));
    public static final GrowthLogicKit MAPLE = new MapleLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "maple"));
    public static final GrowthLogicKit ASPEN = new AspenLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "aspen"));
    public static final GrowthLogicKit TAPERED = new TaperedOakLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "tapered"));
    public static final GrowthLogicKit DIAGONAL_PALM = new DiagonalPalmLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "diagonal_palm"));
    public static final GrowthLogicKit TALL_CONIFER = new ConiferLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "tall_conifer"), 4);
    public static final GrowthLogicKit NORTHERN_CONIFER = new ConiferLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "northern_conifer"), 10).setHorizontalLimiter(2);
    public static final GrowthLogicKit ZELKOVA = new ZelkovaLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "zelkova")).setHorizontalLimiter(2);
    public static final GrowthLogicKit THIN_CONIFER = new PineLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "thin_conifer"));
    public static final GrowthLogicKit BOREAL_CONIFER = new ConiferLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "boreal_conifer")).setHorizontalLimiter(3);
    public static final GrowthLogicKit MEGA_PINE = new MegaPineLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mega_pine"));
    public static final GrowthLogicKit MEGA_FIR = new ConiferLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mega_fir"),4.5f).setHeightVariation(8);
    public static final GrowthLogicKit CIKA = new ConiferLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "cika"),3.5f).setHorizontalLimiter(5).setHeightVariation(8);
    public static final GrowthLogicKit EBONY = new EbonyLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "ebony"));
    public static final GrowthLogicKit REDWOOD = new RedwoodLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "redwood"));
    public static final GrowthLogicKit SMALL_REDWOOD = new SmallRedwoodLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "small_redwood"));
    public static final GrowthLogicKit BAOBAB = new BaobabLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "baobab"));
    public static final GrowthLogicKit CHERRY = new VariateHeightLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "cherry")).setHeightVariation(6);
    public static final GrowthLogicKit SYTHIAN_FUNGUS = new SythianLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "sythian_fungus"));
    public static final GrowthLogicKit CYPRESS = new CypressLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "cypress"));
    public static final GrowthLogicKit MANGROVE = new MangroveLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mangrove"));
    public static final GrowthLogicKit TAPERED_WITHERED = new TaperedWitheredOakLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "tapered_withered"));
    public static final GrowthLogicKit ANCIENT_LOGIC = new AncientLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "ancient"));
    public static final GrowthLogicKit TALL_MAHOGANY = new VariateHeightLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "tall_mahogany")).setHeightVariation(4);

    public static void register(final IRegistry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, MEGA_PRAIRIE, MAPLE, ASPEN, TAPERED, DIAGONAL_PALM,
                TALL_CONIFER, NORTHERN_CONIFER, ZELKOVA, THIN_CONIFER, BOREAL_CONIFER, MEGA_PINE,
                MEGA_FIR, CIKA, EBONY, REDWOOD, SMALL_REDWOOD, BAOBAB, CHERRY, SYTHIAN_FUNGUS,
                CYPRESS, MANGROVE, TAPERED_WITHERED, ANCIENT_LOGIC, TALL_MAHOGANY);
    }

}
