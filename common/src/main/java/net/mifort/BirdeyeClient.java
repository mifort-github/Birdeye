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

    public static final KeyMapping SKIP_ANIMATION =
            new KeyMapping(
                    "key.birdeye.skip_animation",
                    InputConstants.Type.MOUSE,
                    GLFW.GLFW_MOUSE_BUTTON_LEFT,
                    "category.birdeye"
            );

    public static double zoomMul = 3.0;

    public static void init() {
        CONFIG = BirdeyeConfig.load();
    }

    public static void onClientTick(Minecraft client) {
        if (TOGGLE_CAMERA.same(SKIP_ANIMATION)) {
            while (TOGGLE_CAMERA.consumeClick()) {
                SKIP_ANIMATION.consumeClick();

                if (CameraAnimation.isAnimating()) {
                    CameraAnimation.skipAnimation();
                } else {
                    CameraAnimation.toggle(client, zoomMul);
                }
            }
        } else {
            while (TOGGLE_CAMERA.consumeClick()) {
                CameraAnimation.toggle(client, zoomMul);
            }

            boolean animating = CameraAnimation.isAnimating();
            if (animating) {
                while (SKIP_ANIMATION.consumeClick()) {
                    CameraAnimation.skipAnimation();
                }
            } else {
                while (SKIP_ANIMATION.consumeClick()) {

                }
            }
        }

        CameraAnimation.tick(client);
    }
}