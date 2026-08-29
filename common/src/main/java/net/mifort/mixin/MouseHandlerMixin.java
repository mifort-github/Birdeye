package net.mifort.mixin;

import net.mifort.CameraAnimation;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow
    private double accumulatedDX;

    @Shadow
    private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"))
    private void birdeye$clearMouseDelta(CallbackInfo ci) {
        if (CameraAnimation.isCameraModeActive()) {
            accumulatedDX = 0.0;
            accumulatedDY = 0.0;
        }
    }
}
