package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Strafe extends Module {
    public Strafe() {
        super("Strafe", Keyboard.KEY_NONE, Category.COMBAT, "Strafes around targets automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Distance", 1, 5, 2, 0.5));
        addSetting(new Setting("Speed", 1, 10, 5, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (mc.thePlayer.isCollidedHorizontally) {
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw + 90);
            double speed = getSetting("Speed").getDoubleValue() * 0.05;
            mc.thePlayer.motionX += -Math.sin(yaw) * speed;
            mc.thePlayer.motionZ += Math.cos(yaw) * speed;
        }
    }
}