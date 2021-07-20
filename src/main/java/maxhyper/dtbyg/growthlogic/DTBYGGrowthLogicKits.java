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
    public static final GrowthLogicKit DECIDUOUS = new DeciduousOakLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "deciduous"));
    public static final GrowthLogicKit TALL_CONIFER = new ConiferLogic(new ResourceLocation(DynamicTreesBYG.MOD_ID, "tall_conifer"), 4);

    public static void register(final IRegistry<GrowthLogicKit> registry) {
        registry.registerAll(POPLAR, PRAIRIE, MEGA_PRAIRIE, MAPLE, ASPEN, DECIDUOUS, TALL_CONIFER);
    }

}
