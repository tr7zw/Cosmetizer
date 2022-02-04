package dev.tr7zw.cosmetizer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("examplemod")
public class CosmetizerMod extends CosmetizerCore {

    public CosmetizerMod() {

        IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        MOD_BUS.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Hello from the Forge Client setup!");
        init();
    }

}
