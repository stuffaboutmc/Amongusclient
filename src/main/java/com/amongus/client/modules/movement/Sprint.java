package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Sprint extends Module {
    private float lastYaw = 0.0F;
    private boolean wasOmni = false;
    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT, "Auto sprint with omni modes.");
        addSetting(new Setting("Mode", new String[]{"None","Legit","Vanilla","Omni","OmniLegit"}, "Legit"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        boolean idle = mc.thePlayer.moveForward <= 0 && mc.thePlayer.moveStrafing == 0;
        if (idle) {
            if (wasOmni && mode.equals("OmniLegit")) mc.thePlayer.rotationYaw = lastYaw;
            mc.thePlayer.setSprinting(false);
            wasOmni = false;
            return;
        }
        if (mode.equals("Legit")) {
            if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally) mc.thePlayer.setSprinting(true);
        } else if (mode.equals("Vanilla")) {
            if (mc.thePlayer.moveForward > 0 || mc.thePlayer.moveStrafing > 0) mc.thePlayer.setSprinting(true);
        } else if (mode.equals("Omni")) {
            mc.thePlayer.setSprinting(true);
        } else if (mode.equals("OmniLegit")) {
            if (!wasOmni) { lastYaw = mc.thePlayer.rotationYaw; wasOmni = true; }
            float f = mc.thePlayer.moveForward;
            float s = mc.thePlayer.moveStrafing;
            float y = mc.thePlayer.rotationYaw;
            if (f == 0 && s > 0) y -= 90;
            if (f == 0 && s < 0) y += 90;
            if (f > 0 && s > 0) y -= 45;
            if (f > 0 && s < 0) y += 45;
            if (f < 0 && s == 0) y += 180;
            mc.thePlayer.rotationYaw = y;
            mc.thePlayer.setSprinting(true);
        }
    }
    @Override
    public void onDisable() {
        if (wasOmni) { mc.thePlayer.rotationYaw = lastYaw; wasOmni = false; }
        mc.thePlayer.setSprinting(false);
    }
}