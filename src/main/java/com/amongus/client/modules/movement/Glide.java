package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Glide extends Module {
    public Glide() {
        super("Glide", Keyboard.KEY_NONE, Category.MOVEMENT, "Glide down slowly.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("FallSpeed", 1, 10, 2, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.fallDistance > 2 && mc.thePlayer.motionY < 0) {
            mc.thePlayer.motionY = -getSetting("FallSpeed").getDoubleValue() * 0.05;
        }
    }
}