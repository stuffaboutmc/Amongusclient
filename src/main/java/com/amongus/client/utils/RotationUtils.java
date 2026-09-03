package com.amongus.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static float lastYaw, lastPitch;
    public static String rotationMode = "Legit";
    public static boolean silentRotations = false;

    public static float[] getRotations(EntityLivingBase target) {
        Vec3 eyes = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 targetPos = target.getPositionVector().addVector(0, target.height * 0.72, 0);
        return getRotations(targetPos);
    }

    public static float[] getRotations(Vec3 targetPos) {
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
        float factor = getRotationFactor();
        lastYaw = interpolateAngle(lastYaw, targetYaw, factor);
        lastPitch = interpolateAngle(lastPitch, targetPitch, factor);
        return new float[]{lastYaw, lastPitch};
    }

    public static void applyRotations(float yaw, float pitch) {
        if (silentRotations) {
            mc.getNetHandler().addToSendQueue(
                new net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, mc.thePlayer.onGround)
            );
        } else {
            setRotations(yaw, pitch);
        }
    }

    private static float getRotationFactor() {
        switch (rotationMode) {
            case "Legit": return 0.25F;
            case "Custom": return 0.45F;
            case "Snap": return 1.0F;
            case "Smooth": return 0.15F;
            case "Instant": return 1.0F;
            default: return 0.35F;
        }
    }

    private static float getGCD() {
        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return f * f * f * 8.0F;
    }

    private static float interpolateAngle(float from, float to, float factor) {
        float delta = MathHelper.wrapAngleTo180_float(to - from);
        return from + delta * factor;
    }

    public static void setRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }

    public static EntityLivingBase raycastEntity(double range) {
        EntityLivingBase best = null;
        double bestDist = range;
        Vec3 eyes = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 look = mc.thePlayer.getLookVec();
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase && entity = mc.thePlayer) {
                EntityLivingBase living = (EntityLivingBase) entity;
                if (living.isDead || living.getHealth() <= 0) continue;
                double dist = mc.thePlayer.getDistanceToEntity(living);
                if (dist > range) continue;
                if (living.getEntityBoundingBox().expand(0.1, 0.1, 0.1).calculateIntercept(eyes, eyes.addVector(look.xCoord * dist, look.yCoord * dist, look.zCoord * dist)) = null) {
                    if (dist < bestDist) { bestDist = dist; best = living; }
                }
            }
        }
        return best;
    }
}
