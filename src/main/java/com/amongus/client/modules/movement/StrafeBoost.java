package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class StrafeBoost extends Module {
    public StrafeBoost() {
        super("StrafeBoost", Keyboard.KEY_NONE, Category.MOVEMENT, "Boost while strafing.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Boost", 1, 10, 3, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.moveStrafing != 0 && mc.thePlayer.onGround) {
            double boost = getSetting("Boost").getDoubleValue() * 0.05;
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            mc.thePlayer.motionX += Math.cos(yaw) * boost * mc.thePlayer.moveStrafing;
            mc.thePlayer.motionZ += Math.sin(yaw) * boost * mc.thePlayer.moveStrafing;
        }
    }
}