package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Freecam extends Module {
    private double oldX, oldY, oldZ;
    public Freecam() {
        super("Freecam", Keyboard.KEY_NONE, Category.RENDER, "Detached camera.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Speed", 1, 20, 5, 1));
    }
    @Override
    public void onEnable() {
        oldX = mc.thePlayer.posX; oldY = mc.thePlayer.posY; oldZ = mc.thePlayer.posZ;
        mc.thePlayer.noClip = true;
    }
    @Override
    public void onDisable() {
        mc.thePlayer.noClip = false;
        mc.thePlayer.setPosition(oldX, oldY, oldZ);
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        double speed = getSetting("Speed").getDoubleValue() * 0.1;
        mc.thePlayer.motionX = 0; mc.thePlayer.motionY = 0; mc.thePlayer.motionZ = 0;
        if (mc.gameSettings.keyBindJump.isKeyDown()) mc.thePlayer.motionY = speed;
        if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.thePlayer.motionY = -speed;
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double forward = mc.thePlayer.moveForward * speed;
        double strafe = mc.thePlayer.moveStrafing * speed;
        mc.thePlayer.motionX = -Math.sin(yaw) * forward + Math.cos(yaw) * strafe;
        mc.thePlayer.motionZ = Math.cos(yaw) * forward + Math.sin(yaw) * strafe;
    }
}