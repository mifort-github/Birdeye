package net.mifort.mixin;

import net.mifort.CameraAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.gui.GuiMap;

@Mixin(Minecraft.class)
public class BlockMapOpenMixin {

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void birdeye$blockMapWhileAnimating(Screen screen, CallbackInfo ci) {
        if (screen instanceof GuiMap && CameraAnimation.isAnimating()) {
            ci.cancel();
        }
    }
}