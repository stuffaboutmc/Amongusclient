package com.amongus.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C03PacketPlayer;
import java.util.Random;

public class RotationUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static Random random = new Random();

    // Current rotation state
    private static float currentYaw, currentPitch;
    private static boolean initialized = false;

    public static String rotationMode = "Humanized";
    public static boolean silentRotations = false;

    // Custom mode parameters
    public static float customBaseSpeed = 0.3f;
    public static float customAcceleration = 0.4f;
    public static float customNoiseYaw = 0.4f;   // half-range of noise in degrees
    public static float customNoisePitch = 0.2f;

    // For humanized micro-correction
    private static long lastNoiseUpdate = 0;
    private static float currentNoiseYaw = 0;
    private static float currentNoisePitch = 0;

    public static float[] getRotations(EntityLivingBase target) {
        Vec3 eyes = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 targetPos = target.getPositionVector().addVector(0, target.height * 0.72, 0);
        return getRotations(targetPos);
    }

    public static float[] getRotations(Vec3 targetPos) {
        if (!initialized) {
            currentYaw = mc.thePlayer.rotationYaw;
            currentPitch = mc.thePlayer.rotationPitch;
            initialized = true;
        }

        Vec3 eyes = mc.thePlayer.getPositionEyes(1.0F);
        double diffX = targetPos.xCoord - eyes.xCoord;
        double diffY = targetPos.yCoord - eyes.yCoord;
        double diffZ = targetPos.zCoord - eyes.zCoord;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float targetYaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0F;
        float targetPitch = (float) -(Math.atan2(diffY, dist) * 180.0 / Math.PI);

        // Apply GCD fix
        float gcd = getGCD();
        targetYaw = Math.round(targetYaw / gcd) * gcd;
        targetPitch = Math.round(targetPitch / gcd) * gcd;

        // Add noise based on mode
        updateNoise();
        targetYaw += currentNoiseYaw;
        targetPitch += currentNoisePitch;

        float newYaw, newPitch;
        switch (rotationMode) {
            case "Instant":
                newYaw = targetYaw;
                newPitch = targetPitch;
                break;
            case "Normal":
                // Simple linear interpolation
                newYaw = interpolateAngle(currentYaw, targetYaw, 0.5f);
                newPitch = interpolateAngle(currentPitch, targetPitch, 0.5f);
                break;
            case "Humanized":
                newYaw = humanizedYaw(currentYaw, targetYaw);
                newPitch = humanizedPitch(currentPitch, targetPitch);
                break;
            case "Custom":
                newYaw = customYaw(currentYaw, targetYaw);
                newPitch = customPitch(currentPitch, targetPitch);
                break;
            default: // fallback to humanized
                newYaw = humanizedYaw(currentYaw, targetYaw);
                newPitch = humanizedPitch(currentPitch, targetPitch);
        }

        currentYaw = newYaw;
        currentPitch = newPitch;
        return new float[]{currentYaw, currentPitch};
    }

    private static float humanizedYaw(float from, float to) {
        float diff = MathHelper.wrapAngleTo180_float(to - from);
        float absDiff = Math.abs(diff);
        // Base speed and acceleration
        float speed = 0.25f + 0.3f * (absDiff / 180.0f);
        speed *= (1 + (random.nextFloat() - 0.5f) * 0.2f); // jitter
        if (speed > 0.9f) speed = 0.9f;
        if (speed < 0.05f) speed = 0.05f;
        float delta = diff * speed;
        // Occasional overshoot
        if (absDiff < 2.0f && random.nextFloat() < 0.05f) {
            delta += (random.nextFloat() - 0.5f) * 0.3f;
        }
        return from + delta;
    }

    private static float humanizedPitch(float from, float to) {
        float diff = to - from;
        float absDiff = Math.abs(diff);
        float speed = 0.2f + 0.2f * (absDiff / 90.0f);
        speed *= (1 + (random.nextFloat() - 0.5f) * 0.15f);
        if (speed > 0.7f) speed = 0.7f;
        if (speed < 0.03f) speed = 0.03f;
        float delta = diff * speed;
        if (absDiff < 1.0f && random.nextFloat() < 0.05f) {
            delta += (random.nextFloat() - 0.5f) * 0.2f;
        }
        return from + delta;
    }

    private static float customYaw(float from, float to) {
        float diff = MathHelper.wrapAngleTo180_float(to - from);
        float absDiff = Math.abs(diff);
        float speed = customBaseSpeed + customAcceleration * (absDiff / 180.0f);
        speed *= (1 + (random.nextFloat() - 0.5f) * (customNoiseYaw * 0.02f));
        if (speed > 0.95f) speed = 0.95f;
        if (speed < 0.01f) speed = 0.01f;
        return from + diff * speed;
    }

    private static float customPitch(float from, float to) {
        float diff = to - from;
        float absDiff = Math.abs(diff);
        float speed = customBaseSpeed * 0.8f + customAcceleration * 0.8f * (absDiff / 90.0f);
        speed *= (1 + (random.nextFloat() - 0.5f) * (customNoisePitch * 0.02f));
        if (speed > 0.8f) speed = 0.8f;
        if (speed < 0.01f) speed = 0.01f;
        return from + diff * speed;
    }

    private static void updateNoise() {
        long now = System.currentTimeMillis();
        if (now - lastNoiseUpdate > 200 + random.nextInt(300)) {
            float noiseYawRange = rotationMode.equals("Custom") ? customNoiseYaw : 0.4f;
            float noisePitchRange = rotationMode.equals("Custom") ? customNoisePitch : 0.2f;
            currentNoiseYaw = (random.nextFloat() - 0.5f) * noiseYawRange;
            currentNoisePitch = (random.nextFloat() - 0.5f) * noisePitchRange;
            lastNoiseUpdate = now;
        }
    }

    public static void applyRotations(float yaw, float pitch) {
        if (silentRotations) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround));
        } else {
            setRotations(yaw, pitch);
        }
    }

    public static void setRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
        mc.thePlayer.prevRotationYaw = yaw;
        mc.thePlayer.prevRotationPitch = pitch;
    }

    public static EntityLivingBase raycastEntity(double range) {
        EntityLivingBase best = null;
        double bestDist = range;
        Vec3 eyes = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 look = mc.thePlayer.getLookVec();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase && entity != mc.thePlayer) {
                EntityLivingBase living = (EntityLivingBase) entity;
                if (living.isDead || living.getHealth() <= 0) continue;
                double dist = mc.thePlayer.getDistanceToEntity(living);
                if (dist > range) continue;
                if (living.getEntityBoundingBox().expand(0.1, 0.1, 0.1).calculateIntercept(eyes, eyes.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist)) != null) {
                    if (dist < bestDist) {
                        bestDist = dist;
                        best = living;
                    }
                }
            }
        }
        return best;
    }

    private static float getGCD() {
        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return f * f * f * 8.0F;
    }

    private static float interpolateAngle(float from, float to, float factor) {
        float delta = MathHelper.wrapAngleTo180_float(to - from);
        return from + delta * factor;
    }
}
