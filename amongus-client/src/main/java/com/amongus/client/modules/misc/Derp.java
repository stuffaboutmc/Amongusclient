package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Derp extends Module {
    public Derp() {
        super("Derp", Keyboard.KEY_NONE, Category.MISC, "Spins your head randomly.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        mc.thePlayer.rotationYaw = (float) (Math.random() * 360);
        mc.thePlayer.rotationPitch = (float) (Math.random() * 180 - 90);
    }
}