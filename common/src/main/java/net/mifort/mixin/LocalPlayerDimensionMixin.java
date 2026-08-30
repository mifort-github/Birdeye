package net.mifort.mixin;

import net.mifort.CameraAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class LocalPlayerDimensionMixin {

    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void onRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        ResourceKey<Level> oldDimension = client.level.dimension();
        ResourceKey<Level> newDimension = packet.commonPlayerSpawnInfo().dimension();

        if (!oldDimension.equals(newDimension)) {
            if (CameraAnimation.isCameraModeActive()) {
                CameraAnimation.stopAndReturn(client);
            }
        }
    }
}