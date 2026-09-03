package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class InvMove extends Module {
    public InvMove() {
        super("InvMove", Keyboard.KEY_NONE, Category.MOVEMENT, "Move while inventory is open.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.currentScreen != null) {
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            double forward = mc.thePlayer.moveForward * 0.2;
            double strafe = mc.thePlayer.moveStrafing * 0.2;
            mc.thePlayer.motionX = -Math.sin(yaw) * forward + Math.cos(yaw) * strafe;
            mc.thePlayer.motionZ = Math.cos(yaw) * forward + Math.sin(yaw) * strafe;
        }
    }
}