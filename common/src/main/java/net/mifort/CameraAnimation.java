package net.mifort;

import net.mifort.config.BirdeyeConfig;
import net.mifort.mixin.CameraInvoker;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import xaero.lib.client.config.ClientConfigManager;
import xaero.map.WorldMap;
import xaero.map.WorldMapSession;
import xaero.map.common.config.option.WorldMapProfiledConfigOptions;
import xaero.map.gui.GuiMap;

public final class CameraAnimation {

    private static final double HEIGHT = 8.0;
    private static final long ANIMATION_DURATION = 1000L;
    private static final double PLAYER_RENDER_THRESHOLD = 1.0;

    private static boolean detached = false;
    private static boolean animating = false;
    private static boolean returning = false;
    private static boolean playerVisible = false;

    private static long animationStartTime = 0L;
    private static long currentAnimationDuration = ANIMATION_DURATION;

    private static Vec3 playerCameraPosition = Vec3.ZERO;
    private static float playerCameraYaw;
    private static float playerCameraPitch;

    private static Vec3 startPosition = Vec3.ZERO;
    private static float startYaw;
    private static float startPitch;

    private static Vec3 endPosition = Vec3.ZERO;
    private static float endYaw;
    private static float endPitch;

    private static Vec3 detachedPosition = Vec3.ZERO;
    private static float detachedYaw;
    private static float detachedPitch;

    private static float lockedPlayerYaw;
    private static float lockedPlayerPitch;

    private static double zoomMul;
    private static double fov = 70.0;
    private static double renderDistance = 1.0;

    private static double animationHeight = 0.0;

    private static long pauseStartedAt = 0L;

    private CameraAnimation() {

    }

    private static double calc() {
        BirdeyeConfig config = BirdeyeClient.CONFIG;

        double maxHeight = 8.0 * renderDistance;

        Minecraft mc = Minecraft.getInstance();

        LocalPlayer player = mc.player;

        double eyeLevel = player.getEyeY() - player.getY();

        double screenShortSide = Math.min(mc.getWindow().getWidth(), mc.getWindow().getHeight());

        double ScaleMultiplier = (screenShortSide <= 1080) ? 1.0D : (screenShortSide / 1080.0D);

        ClientConfigManager configManager = WorldMap.INSTANCE.getConfigs().getClientConfigManager();
        boolean openingAnimationConfig = configManager.getEffective(WorldMapProfiledConfigOptions.OPENING_ANIMATION);


        double idk = returning ? 1.0 : openingAnimationConfig ? 1.5 : 1.0;

        double length = mc.getWindow().getHeight() / (2.0 / zoomMul * idk * ScaleMultiplier);

        double heightChange = length / Math.tan(Math.toRadians(0.5*fov)) - eyeLevel;

        double h = !config.UNLOCK_HEIGHT ? Math.min(heightChange, maxHeight) : heightChange;

        return getSkyObstructionDistance(Minecraft.getInstance(), playerCameraPosition, h);
    }

    private static long getAnimationDuration(double height) {
        BirdeyeConfig config = BirdeyeClient.CONFIG;

        double multiplier = height / HEIGHT;
        multiplier = Mth.clamp(multiplier, config.ANIMATION_DURATION_MIN, config.ANIMATION_DURATION_MAX);

        return (long) (ANIMATION_DURATION * multiplier * config.ANIMATION_DURATION_MUL);
    }

    public static void openMap(Minecraft minecraft, double zoomMultiplier) {
        if (animating || detached) {
            return;
        }

        WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();

        if (worldmapSession == null || !worldmapSession.isUsable()) {
            return;
        }

        Entity entity = minecraft.getCameraEntity();

        if (entity == null) {
            return;
        }

        zoomMul = 1 / zoomMultiplier;
        fov = minecraft.options.fov().get();
        renderDistance = minecraft.options.renderDistance().get();

        Camera camera = minecraft.gameRenderer.getMainCamera();

        Vec3 currentPosition = camera.getPosition();
        float currentYaw = camera.getYRot();
        float currentPitch = camera.getXRot();

        playerCameraPosition = currentPosition;
        playerCameraYaw = currentYaw;
        playerCameraPitch = currentPitch;

        lockedPlayerYaw = entity.getYRot();
        lockedPlayerPitch = entity.getXRot();

        startPosition = currentPosition;
        startYaw = currentYaw;
        startPitch = currentPitch;

        double height = calc();
        animationHeight = height;

        endPosition = currentPosition.add(0.0, height, 0.0);
        endYaw = 180.0f;
        endPitch = 90.0f;

        currentAnimationDuration = getAnimationDuration(height);

        detached = true;
        returning = false;
        animating = true;
        playerVisible = false;

        pauseStartedAt = 0L;
        animationStartTime = System.currentTimeMillis();
    }

    public static void closeMap(Minecraft minecraft, double zoomMultiplier) {

        if (animating || !detached) {
            return;
        }

        zoomMul = 1 / zoomMultiplier;
        fov = minecraft.options.fov().get();
        renderDistance = minecraft.options.renderDistance().get();

        returning = true;

        double returnHeight = calc();
        animationHeight = returnHeight;

        startPosition = new Vec3(detachedPosition.x, playerCameraPosition.y + returnHeight, detachedPosition.z);

        startYaw = detachedYaw;
        startPitch = detachedPitch;

        endPosition = playerCameraPosition;
        endYaw = playerCameraYaw;
        endPitch = playerCameraPitch;

        currentAnimationDuration = getAnimationDuration(returnHeight);

        animating = true;

        pauseStartedAt = 0L;
        animationStartTime = System.currentTimeMillis();
    }

    public static void toggle(Minecraft minecraft, double zoomMultiplier) {
        if (detached) {
            closeMap(minecraft, zoomMultiplier);
        } else {
            openMap(minecraft, zoomMultiplier);
        }
    }

    public static void tick(Minecraft minecraft) {
        if (Minecraft.getInstance().isPaused()) {
            return;
        }

        if (!isCameraModeActive()) {
            return;
        }

        Entity entity = minecraft.getCameraEntity();

        if (entity == null) {
            return;
        }

        entity.setYRot(lockedPlayerYaw);
        entity.setXRot(lockedPlayerPitch);

        if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
            living.setYBodyRot(lockedPlayerYaw);
            living.setYHeadRot(lockedPlayerYaw);
        }
    }

    public static void apply(Camera camera, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        if (detached && !animating) {
            updatePlayerVisibility(mc, detachedPosition);
            applyTransform(camera, detachedPosition, detachedYaw, detachedPitch);
            return;
        }

        if (!animating || mc.player == null) {
            return;
        }

        if (mc.isPaused()) {
            if (pauseStartedAt == 0L) {
                pauseStartedAt = System.currentTimeMillis();
            }
            return;
        }

        if (pauseStartedAt != 0L) {
            animationStartTime += System.currentTimeMillis() - pauseStartedAt;
            pauseStartedAt = 0L;
        }

        Vec3 playerPos = mc.player.getEyePosition(partialTick);
        double hh = getSkyObstructionDistance(mc, playerPos, animationHeight);

        if (returning) {
            startPosition = playerPos.add(0, hh, 0);
            endPosition = playerPos;
        } else {
            startPosition = playerPos;
            endPosition = playerPos.add(0, hh, 0);
        }

        long elapsed = System.currentTimeMillis() - animationStartTime;

        float progress = (float) elapsed / (float) currentAnimationDuration;
        progress = Mth.clamp(progress, 0.0f, 1.0f);

        float t = progress * progress * (3.0f - 2.0f);

        double x = Mth.lerp(t, startPosition.x, endPosition.x);
        double y = Mth.lerp(t, startPosition.y, endPosition.y);
        double z = Mth.lerp(t, startPosition.z, endPosition.z);

        float yaw = lerpAngle(startYaw, endYaw, t);
        float pitch = Mth.lerp(t, startPitch, endPitch);

        Vec3 currentPosition = new Vec3(x, y, z);
        updatePlayerVisibility(mc, currentPosition);
        applyTransform(camera, currentPosition, yaw, pitch);

        if (progress >= 1.0f) {
            updatePlayerVisibility(mc, endPosition);
            applyTransform(camera, endPosition, endYaw, endPitch);

            animating = false;

            if (returning) {
                returning = false;
                detached = false;
                playerVisible = false;

                if (mc.screen instanceof GuiMap) {
                    mc.setScreen(null);
                }

                return;
            }

            detachedPosition = endPosition;
            detachedYaw = endYaw;
            detachedPitch = endPitch;

            WorldMapSession worldmapSession = WorldMapSession.getCurrentSession();

            if (worldmapSession == null || !worldmapSession.isUsable()) {
                detached = false;
                playerVisible = false;
                return;
            }

            if (!(mc.screen instanceof GuiMap)) {
                mc.setScreen(new GuiMap(null, null, worldmapSession.getMapProcessor(), mc.getCameraEntity()));
            }
        }
    }

    private static void updatePlayerVisibility(Minecraft minecraft, Vec3 cameraPosition) {
        LocalPlayer player = minecraft.player;

        if (player == null) {
            playerVisible = false;
            return;
        }

        double heightAboveEyes = cameraPosition.y - player.getEyeY();
        playerVisible = heightAboveEyes >= PLAYER_RENDER_THRESHOLD;
    }

    private static void applyTransform(Camera camera, Vec3 position, float yaw, float pitch) {
        CameraInvoker invoker = (CameraInvoker) camera;

        invoker.birdeye$setPosition(position.x, position.y, position.z);

        invoker.birdeye$setRotation(yaw, pitch);
    }

    private static float lerpAngle(float start, float end, float t) {
        float difference = Mth.wrapDegrees(end - start);
        return start + difference * t;
    }

    public static boolean isCameraModeActive() {
        return detached || animating;
    }

    public static boolean isDetached() {
        return detached;
    }

    public static boolean isAnimating() {
        return animating;
    }

    public static boolean isPlayerVisible() {
        return isCameraModeActive() && playerVisible;
    }

    private static double getSkyObstructionDistance(Minecraft minecraft, Vec3 position, double maxDistance) {
        if (minecraft.level == null) {
            return maxDistance;
        }

        int startY = Mth.floor(position.y);

        for (int y = startY + 1; y <= startY + maxDistance; y++) {
            BlockPos blockPos = new BlockPos(
                    Mth.floor(position.x),
                    y,
                    Mth.floor(position.z)
            );

            BlockState state = minecraft.level.getBlockState(blockPos);

            if (!state.isAir() && state.isViewBlocking(minecraft.level, blockPos)) {
                return y - position.y;
            }
        }

        return maxDistance;
    }

    public static void stopAndReturn(Minecraft minecraft) {
        if (!isCameraModeActive()) {
            return;
        }

        Camera camera = minecraft.gameRenderer.getMainCamera();

        applyTransform(
                camera,
                playerCameraPosition,
                playerCameraYaw,
                playerCameraPitch
        );

        if (minecraft.player != null) {
            minecraft.setCameraEntity(minecraft.player);
        }

        animating = false;
        returning = false;
        detached = false;
        playerVisible = false;
    }

    public static void skipAnimation() {
        if (!animating) {
            return;
        }

        animationStartTime = System.currentTimeMillis() - currentAnimationDuration;

        pauseStartedAt = 0L;
    }

}