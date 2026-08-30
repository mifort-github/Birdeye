package net.mifort.neoforge;

import net.mifort.BirdeyeClient;
import net.mifort.config.BirdeyeConfig;
import net.mifort.config.BirdeyeConfigScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = BirdeyeClient.MOD_ID, dist = Dist.CLIENT)
public class BirdeyeNeoForge {

    public BirdeyeNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerKeyMappings);
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (minecraft, parentScreen) -> BirdeyeConfigScreen.create(parentScreen)
        );
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        BirdeyeConfig.CONFIG_DIR = FMLPaths.CONFIGDIR.get();
        BirdeyeClient.init();
    }

    private void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(BirdeyeClient.TOGGLE_CAMERA);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        BirdeyeClient.onClientTick(Minecraft.getInstance());
    }
}