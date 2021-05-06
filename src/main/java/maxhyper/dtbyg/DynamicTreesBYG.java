package maxhyper.dtbyg;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import corgiaoc.byg.BYG;
import maxhyper.dtbyg.init.DTBYGClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesBYG.MOD_ID)
public class DynamicTreesBYG
{
    public static final String MOD_ID = "dtbyg";

    public DynamicTreesBYG() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        BYG.ENABLE_OVERWORLD_TREES = true;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        //DTBYGClient.setup();
    }

}
