package maxhyper.dtbyg;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import corgiaoc.byg.BYG;
import maxhyper.dtbyg.init.DTBYGClient;
import maxhyper.dtbyg.init.DTBYGRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DynamicTreesBYG.MOD_ID)
public class DynamicTreesBYG
{
    public static final String MOD_ID = "dtbyg";

    public DynamicTreesBYG() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        DTBYGRegistries.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        if (DTConfigs.WORLD_GEN.get()){
            BYG.ENABLE_OVERWORLD_TREES = false;
            if (ModList.get().isLoaded("dynamictreesplus")) {
                BYG.ENABLE_CACTI = false;
            }
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DTBYGClient.setup();
    }

    public static ResourceLocation resLoc (final String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
