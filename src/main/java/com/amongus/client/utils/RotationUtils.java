package com.amongus.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C03PacketPlayer;

public class RotationUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static String rotationMode = "Humanized";
    public static boolean silentRotations = false;

    private static float currentYaw;
    private static float currentPitch;
    private static boolean initialized = false;

    public static float customBaseSpeed = 0.3f;
    public static float customAcceleration = 0.4f;
    public static float customNoiseYaw = 0.4f;
    public static float customNoisePitch = 0.2f;

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

        float gcd = getGCD();
        targetYaw = Math.round(targetYaw / gcd) * gcd;
        targetPitch = Math.round(targetPitch / gcd) * gcd;

        // Add noise using Math.random()
        float yawNoise = (float)(Math.random() - 0.5) * 0.4f;
        float pitchNoise = (float)(Math.random() - 0.5) * 0.2f;
        targetYaw += yawNoise;
        targetPitch += pitchNoise;

        switch (rotationMode) {
            case "Instant":
                currentYaw = targetYaw;
                currentPitch = targetPitch;
                break;
            case "Normal":
                currentYaw = interpolateAngle(currentYaw, targetYaw, 0.5f);
                currentPitch = interpolateAngle(currentPitch, targetPitch, 0.5f);
                break;
            case "Custom":
                currentYaw = customInterpolateYaw(currentYaw, targetYaw);
                currentPitch = customInterpolatePitch(currentPitch, targetPitch);
                break;
            case "Humanized":
            default:
                currentYaw = humanizedYaw(currentYaw, targetYaw);
                currentPitch = humanizedPitch(currentPitch, targetPitch);
                break;
        }

        return new float[]{currentYaw, currentPitch};
    }

    private static float humanizedYaw(float from, float to) {
        float diff = MathHelper.wrapAngleTo180_float(to - from);
        float absDiff = Math.abs(diff);
        float speed = 0.25f + 0.3f * (absDiff / 180.0f);
        speed *= (1.0f + (float)(Math.random() - 0.5) * 0.2f);
        if (speed > 0.9f) speed = 0.9f;
        if (speed < 0.05f) speed = 0.05f;
        float delta = diff * speed;
        if (absDiff < 2.0f && Math.random() < 0.05f) {
            delta += (float)(Math.random() - 0.5) * 0.3f;
        }
        return from + delta;
    }

    private static float humanizedPitch(float from, float to) {
        float diff = to - from;
        float absDiff = Math.abs(diff);
        float speed = 0.2f + 0.2f * (absDiff / 90.0f);
        speed *= (1.0f + (float)(Math.random() - 0.5) * 0.15f);
        if (speed > 0.7f) speed = 0.7f;
        if (speed < 0.03f) speed = 0.03f;
        float delta = diff * speed;
        if (absDiff < 1.0f && Math.random() < 0.05f) {
            delta += (float)(Math.random() - 0.5) * 0.2f;
        }
        return from + delta;
    }

    private static float customInterpolateYaw(float from, float to) {
        float diff = MathHelper.wrapAngleTo180_float(to - from);
        float absDiff = Math.abs(diff);
        float speed = customBaseSpeed + customAcceleration * (absDiff / 180.0f);
        speed *= (1.0f + (float)(Math.random() - 0.5) * (customNoiseYaw * 0.02f));
        if (speed > 0.95f) speed = 0.95f;
        if (speed < 0.01f) speed = 0.01f;
        return from + diff * speed;
    }

    private static float customInterpolatePitch(float from, float to) {
        float diff = to - from;
        float absDiff = Math.abs(diff);
        float speed = customBaseSpeed * 0.8f + customAcceleration * 0.8f * (absDiff / 90.0f);
        speed *= (1.0f + (float)(Math.random() - 0.5) * (customNoisePitch * 0.02f));
        if (speed > 0.8f) speed = 0.8f;
        if (speed < 0.01f) speed = 0.01f;
        return from + diff * speed;
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
