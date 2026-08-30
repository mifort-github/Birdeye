package net.mifort.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.mifort.BirdeyeClient;
import net.mifort.CameraAnimation;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.map.gui.GuiMap;

@Mixin(GuiMap.class)
public class CloseMixin {

    @Shadow
    private static double destScale;

    @Inject(method = "onInputPress", at = @At("HEAD"))
    public void onInputPress(InputConstants.Type type, int code, CallbackInfoReturnable<Boolean> cir) {
        InputConstants.Key pressed = type.getOrCreate(code);
        InputConstants.Key bound = ((KeyMappingAccessor) BirdeyeClient.TOGGLE_CAMERA).getKey();

        if (pressed.equals(bound)) {
            ((GuiMap) (Object) this).goBack();

            Minecraft client = Minecraft.getInstance();
            CameraAnimation.closeMap(client, destScale);
        }
    }

    @Inject(method = "removed", at = @At("HEAD"))
    public void onInputPress(CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        CameraAnimation.closeMap(client, destScale);
    }

    @Inject(method = "applyZoomLimits", at = @At("HEAD"), require = 0)
    public void onApplyZoomLimits(CallbackInfo ci) {
        BirdeyeClient.zoomMul = destScale;
    }

    @Inject(method = "changeZoom", at = @At("HEAD"), require = 0)
    public void onApplyChangeZoom(CallbackInfo ci) {
        BirdeyeClient.zoomMul = destScale;
    }
}
