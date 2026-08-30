package net.mifort.mixin;

import net.mifort.CameraAnimation;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    Minecraft minecraft;

    @Unique
    private static Boolean birdeye$fovReturnsFloat = null;

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void birdeye$hideHand(CallbackInfo ci) {
        if (CameraAnimation.isCameraModeActive()) {
            ci.cancel();
        }
    }

    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void myMod$suppressDynamicFov(Camera camera, float partialTick, boolean useFovSetting, CallbackInfoReturnable<Object> cir) {
        if (!CameraAnimation.isAnimating()) {
            return;
        }

        double baseFov = this.minecraft.options.fov().get();

        if (birdeye$getFovReturnsFloat()) {
            cir.setReturnValue((float) baseFov);
        } else {
            cir.setReturnValue(baseFov);
        }
    }

    @Unique
    private static boolean birdeye$getFovReturnsFloat() {
        if (birdeye$fovReturnsFloat == null) {
            boolean isFloat = true;

            for (Method method : GameRenderer.class.getDeclaredMethods()) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 3
                        && params[0] == Camera.class
                        && params[1] == float.class
                        && params[2] == boolean.class) {
                    isFloat = method.getReturnType() == float.class;
                    break;
                }
            }

            birdeye$fovReturnsFloat = isFloat;
        }

        return birdeye$fovReturnsFloat;
    }
}