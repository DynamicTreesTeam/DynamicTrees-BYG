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
    public static final GrowthLogicKit THIN_CONIFER = new PineLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "thin_conifer"));
    public static final GrowthLogicKit BOREAL_CONIFER = new ConiferLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "boreal_conifer")).setHorizontalLimiter(3);
    public static final GrowthLogicKit MEGA_PINE = new MegaPineLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mega_pine"));
    public static final GrowthLogicKit MEGA_FIR = new ConiferLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "mega_fir"),4.5f).setHeightVariation(8);
    public static final GrowthLogicKit EBONY = new EbonyLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "ebony"));
    public static final GrowthLogicKit REDWOOD = new RedwoodLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "redwood"));
    public static final GrowthLogicKit SMALL_REDWOOD = new SmallRedwoodLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "small_redwood"));

    public static void register(final IRegistry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, MEGA_PRAIRIE, MAPLE, ASPEN, TAPERED, DIAGONAL_PALM,
                TALL_CONIFER, NORTHERN_CONIFER, THIN_CONIFER, BOREAL_CONIFER, MEGA_PINE,
                MEGA_FIR, EBONY, REDWOOD, SMALL_REDWOOD);
    }

}
