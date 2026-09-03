package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Fly extends Module {
    public Fly() {
        super("Fly", Keyboard.KEY_NONE, Category.MOVEMENT, "Allows you to fly.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Speed", 1, 10, 3, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        double speed = getSetting("Speed").getDoubleValue() * 0.1;
        mc.thePlayer.motionY = 0;
        if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = speed;
        if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = -speed;
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double forward = mc.thePlayer.moveForward * speed;
        double strafe = mc.thePlayer.moveStrafing * speed;
        mc.thePlayer.motionX = -Math.sin(yaw) * forward + Math.cos(yaw) * strafe;
        mc.thePlayer.motionZ = Math.cos(yaw) * forward + Math.sin(yaw) * strafe;
    }
    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.motionY = 0;
    }
}