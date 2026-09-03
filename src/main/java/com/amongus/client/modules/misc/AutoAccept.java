package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoAccept extends Module {
    public AutoAccept() {
        super("AutoAccept", Keyboard.KEY_NONE, Category.MISC, "Accepts party invites.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (getSetting("Mode").getValue().equals("None")) return;
        String text = event.message.getUnformattedText();
        if (text.contains("party") && text.contains("invite")) {
            mc.thePlayer.sendChatMessage("/party accept");
        }
    }
}