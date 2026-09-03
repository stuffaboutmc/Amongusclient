package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Phase extends Module {
    public Phase() {
        super("Phase", Keyboard.KEY_NONE, Category.MOVEMENT, "Phase through blocks.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.noClip = true;
            mc.thePlayer.motionY = -0.1;
        } else if (mc.thePlayer.onGround) {
            mc.thePlayer.noClip = false;
        }
    }
    @Override
    public void onDisable() { mc.thePlayer.noClip = false; }
}