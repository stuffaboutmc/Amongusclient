package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class NoSwing extends Module {
    public NoSwing() {
        super("NoSwing", Keyboard.KEY_NONE, Category.MISC, "No swing animation.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        mc.thePlayer.isSwingInProgress = false;
        mc.thePlayer.swingProgress = 0;
        mc.thePlayer.prevSwingProgress = 0;
    }
}