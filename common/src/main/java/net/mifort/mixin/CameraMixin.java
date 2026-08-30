package net.mifort.mixin;

import net.mifort.CameraAnimation;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @ModifyVariable(method = "setup", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private boolean birdeye$forceThirdPerson(boolean thirdPerson) {
        if (CameraAnimation.isPlayerVisible() && !Minecraft.getInstance().isPaused()) {
            return true;
        }

        return thirdPerson;
    }

    @Inject(method = "setup", at = @At("TAIL"))
    private void birdeye$applyCameraAnimation(BlockGetter level, Entity entity, boolean thirdPerson, boolean mirrored, float partialTick, CallbackInfo ci) {
        CameraAnimation.apply((Camera) (Object) this, partialTick);
    }
}