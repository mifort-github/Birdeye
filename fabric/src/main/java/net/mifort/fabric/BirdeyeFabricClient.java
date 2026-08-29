package net.mifort.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.mifort.BirdeyeClient;
import net.mifort.config.BirdeyeConfig;

public final class BirdeyeFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BirdeyeConfig.CONFIG_DIR = FabricLoader.getInstance().getConfigDir();

        BirdeyeClient.init();

        KeyBindingHelper.registerKeyBinding(BirdeyeClient.TOGGLE_CAMERA);

        ClientTickEvents.END_CLIENT_TICK.register(BirdeyeClient::onClientTick);
    }
}