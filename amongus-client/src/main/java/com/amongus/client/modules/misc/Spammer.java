package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Spammer extends Module {
    private long lastSend = 0;
    public Spammer() {
        super("Spammer", Keyboard.KEY_NONE, Category.MISC, "Spams chat messages.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Delay", 1000, 10000, 3000, 500));
        addSetting(new Setting("Message", new String[]{"Amongus Client on top!","Get Amongus Client!","ez"}, "Amongus Client on top!"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (System.currentTimeMillis() - lastSend >= (int) getSetting("Delay").getDoubleValue()) {
            mc.thePlayer.sendChatMessage(getSetting("Message").getValue());
            lastSend = System.currentTimeMillis();
        }
    }
}