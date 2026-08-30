package net.mifort.forge;

import net.mifort.BirdeyeClient;
import net.mifort.config.BirdeyeConfig;
import net.mifort.config.BirdeyeConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(BirdeyeClient.MOD_ID)
public class BirdeyeForge {

    public BirdeyeForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeyMappings);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parentScreen) -> BirdeyeConfigScreen.create(parentScreen)
                )
        );
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        BirdeyeConfig.CONFIG_DIR = FMLPaths.CONFIGDIR.get();
        BirdeyeClient.init();
    }

    private void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BirdeyeClient.TOGGLE_CAMERA);
        event.register(BirdeyeClient.SKIP_ANIMATION);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            BirdeyeClient.onClientTick(Minecraft.getInstance());
        }
    }
}