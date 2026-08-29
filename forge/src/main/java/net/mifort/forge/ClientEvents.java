package net.mifort.forge;

import net.mifort.BirdeyeClient;
import net.mifort.CameraAnimation;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BirdeyeClient.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        if (CameraAnimation.isCameraModeActive()) {
            event.setCanceled(true);
        }
    }
}
