package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class LongJump extends Module {
    public LongJump() {
        super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT, "Jump farther.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Boost", 1, 5, 2, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround) {
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            double boost = getSetting("Boost").getDoubleValue() * 0.5;
            mc.thePlayer.motionX += -Math.sin(yaw) * boost;
            mc.thePlayer.motionZ += Math.cos(yaw) * boost;
            mc.thePlayer.jump();
        }
    }
}