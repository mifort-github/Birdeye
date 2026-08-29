package net.mifort.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mifort.CameraAnimation;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void hideHand(PoseStack poseStack, Camera camera, float f, CallbackInfo ci) {
        if (CameraAnimation.isCameraModeActive()) {
            ci.cancel();
        }
    }

    @Shadow
    @Final
    Minecraft minecraft;
    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    private void myMod$suppressDynamicFov(Camera camera, float partialTick, boolean useFovSetting, CallbackInfoReturnable<Double> cir) {
        if (CameraAnimation.isAnimating()) {
            double baseFov = this.minecraft.options.fov().get();
            cir.setReturnValue(baseFov);
        }
    }
}
