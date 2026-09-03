package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Speed extends Module {
    public Speed() {
        super("Speed", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes you faster.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced","Bhop"}, "Basic"));
        addSetting(new Setting("Multiplier", 1, 10, 2, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (!mc.thePlayer.isMoving()) return;
        double mult = getSetting("Multiplier").getDoubleValue();
        if (mode.equals("Basic")) {
            mc.thePlayer.motionX *= mult;
            mc.thePlayer.motionZ *= mult;
        } else if (mode.equals("Advanced")) {
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            double boost = 0.3 * mult;
            mc.thePlayer.motionX += -Math.sin(yaw) * boost;
            mc.thePlayer.motionZ += Math.cos(yaw) * boost;
        } else if (mode.equals("Bhop")) {
            if (mc.thePlayer.onGround) mc.thePlayer.jump();
            mc.thePlayer.motionX *= 1.5;
            mc.thePlayer.motionZ *= 1.5;
        }
    }
}