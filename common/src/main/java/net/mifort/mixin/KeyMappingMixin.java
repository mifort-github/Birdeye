package net.mifort.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.mifort.BirdeyeClient;
import net.mifort.CameraAnimation;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {

    @Inject(method = "click", at = @At("HEAD"), cancellable = true)
    private static void birdeye$click(InputConstants.Key key, CallbackInfo ci) {
        boolean animating = CameraAnimation.isAnimating();

        for (KeyMapping mapping : KeyMappingAccessor.birdeye$getAll().values()) {
            KeyMappingAccessor accessor = (KeyMappingAccessor) mapping;
            if (!accessor.birdeye$getKey().equals(key)) continue;

            boolean isBirdeyeKey = mapping == BirdeyeClient.TOGGLE_CAMERA
                    || mapping == BirdeyeClient.SKIP_ANIMATION;

            if (animating && !isBirdeyeKey) {
                continue;
            }

            accessor.birdeye$setClickCount(accessor.birdeye$getClickCount() + 1);
        }
        ci.cancel();
    }

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private static void birdeye$set(InputConstants.Key key, boolean down, CallbackInfo ci) {
        boolean animating = CameraAnimation.isAnimating();

        for (KeyMapping mapping : KeyMappingAccessor.birdeye$getAll().values()) {
            KeyMappingAccessor accessor = (KeyMappingAccessor) mapping;
            if (!accessor.birdeye$getKey().equals(key)) continue;

            boolean isBirdeyeKey = mapping == BirdeyeClient.TOGGLE_CAMERA || mapping == BirdeyeClient.SKIP_ANIMATION;

            if (animating && !isBirdeyeKey && down) {
                continue;
            }

            mapping.setDown(down);
        }
        ci.cancel();
    }
}