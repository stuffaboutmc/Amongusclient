package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    private float lastYaw = 0.0F;
    private boolean wasOmni = false;

    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT, "Auto sprint with omni-directional modes.");
        addSetting(new Setting("Mode", new String[]{"None", "Legit", "Vanilla", "Omni", "OmniLegit"}, "Legit"));
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;

        boolean idle = mc.thePlayer.moveForward <= 0 && mc.thePlayer.moveStrafing == 0;
        if (idle) {
            if (wasOmni && mode.equals("OmniLegit")) {
                mc.thePlayer.rotationYaw = lastYaw;
            }
            mc.thePlayer.setSprinting(false);
            wasOmni = false;
            return;
        }

        if (mode.equals("Legit")) {
            if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally) {
                mc.thePlayer.setSprinting(true);
            }
        } else if (mode.equals("Vanilla")) {
            if (mc.thePlayer.moveForward > 0 || mc.thePlayer.moveStrafing > 0) {
                mc.thePlayer.setSprinting(true);
            }
        } else if (mode.equals("Omni")) {
            mc.thePlayer.setSprinting(true);
        } else if (mode.equals("OmniLegit")) {
            if (!wasOmni) {
                lastYaw = mc.thePlayer.rotationYaw;
                wasOmni = true;
            }
            float moveYaw = getMoveYaw();
            mc.thePlayer.rotationYaw = moveYaw;
            mc.thePlayer.setSprinting(true);
        }
    }

    private float getMoveYaw() {
        float forward = mc.thePlayer.moveForward;
        float strafe = mc.thePlayer.moveStrafing;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0 && strafe > 0) {
            return yaw - 90.0F;
        } else if (forward == 0 && strafe < 0) {
            return yaw + 90.0F;
        } else if (forward > 0 && strafe == 0) {
            return yaw;
        } else if (forward > 0 && strafe > 0) {
            return yaw - 45.0F;
        } else if (forward > 0 && strafe < 0) {
            return yaw + 45.0F;
        } else if (forward < 0 && strafe == 0) {
            return yaw + 180.0F;
        } else if (forward < 0 && strafe > 0) {
            return yaw + 135.0F;
        } else if (forward < 0 && strafe < 0) {
            return yaw - 135.0F;
        }
        return yaw;
    }

    @Override
    public void onDisable() {
        if (wasOmni) {
            mc.thePlayer.rotationYaw = lastYaw;
            wasOmni = false;
        }
        mc.thePlayer.setSprinting(false);
    }
}
