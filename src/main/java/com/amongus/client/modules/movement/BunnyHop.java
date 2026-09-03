package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class BunnyHop extends Module {
    public BunnyHop() {
        super("BunnyHop", Keyboard.KEY_NONE, Category.MOVEMENT, "Bunny hop for speed.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Boost", 1, 10, 3, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
            mc.thePlayer.jump();
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            double boost = getSetting("Boost").getDoubleValue() * 0.1;
            mc.thePlayer.motionX += -Math.sin(yaw) * boost;
            mc.thePlayer.motionZ += Math.cos(yaw) * boost;
        }
    }
}