package maxhyper.dtbyg;

import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import maxhyper.dtbyg.init.DTBYGClient;
import maxhyper.dtbyg.init.DTBYGRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DynamicTreesBYG.MOD_ID)
public class DynamicTreesBYG {

    public static final String MOD_ID = "dtbyg";

    public DynamicTreesBYG() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::gatherData);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, DTBYGRegistries::onBiomeLoading);

        RegistryHandler.setup(MOD_ID);

        DTBYGRegistries.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DTBYGClient.setup();
    }

    public void gatherData(final GatherDataEvent event) {
        GatherDataHelper.gatherTagData(MOD_ID, event);
        GatherDataHelper.gatherLootData(MOD_ID, event);
    }

    public static ResourceLocation resLoc(final String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
