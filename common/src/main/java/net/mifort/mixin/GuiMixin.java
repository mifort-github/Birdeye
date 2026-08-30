package net.mifort.mixin;

import net.mifort.CameraAnimation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = 2000)
public class GuiMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void birdeye$blockHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (CameraAnimation.isCameraModeActive()) {
            ci.cancel();
        }
    }
}