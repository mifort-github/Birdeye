package net.mifort.neoforge;

import net.mifort.BirdeyeClient;
import net.mifort.CameraAnimation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = BirdeyeClient.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        if (CameraAnimation.isCameraModeActive()) {
            event.setCanceled(true);
        }
    }
}
