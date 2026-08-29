package net.mifort.mixin;

import net.mifort.CameraAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class LocalPlayerDeathMixin {
    @Inject(method = "handlePlayerCombatKill", at = @At("HEAD"))
    private void onCombatKill(ClientboundPlayerCombatKillPacket packet, CallbackInfo ci) {
        if (CameraAnimation.isCameraModeActive()) {
            CameraAnimation.stopAndReturn(Minecraft.getInstance());
        }
    }
}