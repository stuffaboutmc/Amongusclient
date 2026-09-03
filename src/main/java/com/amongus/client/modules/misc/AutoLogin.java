package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoLogin extends Module {
    public AutoLogin() {
        super("AutoLogin", Keyboard.KEY_NONE, Category.MISC, "Logs in automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Password", new String[]{"password","changeme","12345"}, "password"));
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (getSetting("Mode").getValue().equals("None")) return;
        String text = event.message.getUnformattedText();
        if (text.contains("login") || text.contains("register")) {
            mc.thePlayer.sendChatMessage("/login " + getSetting("Password").getValue());
        }
    }
}
