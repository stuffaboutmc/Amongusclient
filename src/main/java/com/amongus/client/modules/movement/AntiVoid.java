package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", Keyboard.KEY_NONE, Category.MOVEMENT, "Prevents falling into void.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Threshold", 1, 20, 5, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.posY < getSetting("Threshold").getDoubleValue()) {
            mc.thePlayer.motionY = 1.0;
            mc.thePlayer.capabilities.isFlying = true;
        } else if (mc.thePlayer.capabilities.isFlying) {
            mc.thePlayer.capabilities.isFlying = false;
        }
    }
    @Override
    public void onDisable() { mc.thePlayer.capabilities.isFlying = false; }
}