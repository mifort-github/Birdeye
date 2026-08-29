package net.mifort;

import com.mojang.blaze3d.platform.InputConstants;
import net.mifort.config.BirdeyeConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class BirdeyeClient {

    public static final String MOD_ID = "birdeye";

    public static BirdeyeConfig CONFIG;

    public static final KeyMapping TOGGLE_CAMERA =
            new KeyMapping(
                    "key.birdeye.toggle_camera",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_N,
                    "category.birdeye"
            );

    public static double zoomMul = 3.0;

    public static void init() {
        CONFIG = BirdeyeConfig.load();
    }

    public static void onClientTick(Minecraft client) {
        while (TOGGLE_CAMERA.consumeClick()) {
            CameraAnimation.toggle(client, zoomMul);
        }

        CameraAnimation.tick(client);
    }
}