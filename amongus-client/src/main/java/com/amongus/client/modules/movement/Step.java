package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Step extends Module {
    public Step() {
        super("Step", Keyboard.KEY_NONE, Category.MOVEMENT, "Steps up blocks.");
        addSetting(new Setting("Height", 1, 10, 2, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.stepHeight = (float) getSetting("Height").getDoubleValue();
        } else {
            mc.thePlayer.stepHeight = 0.5F;
        }
    }
    @Override
    public void onDisable() { mc.thePlayer.stepHeight = 0.5F; }
}