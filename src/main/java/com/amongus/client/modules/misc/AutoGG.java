package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoGG extends Module {
    public AutoGG() {
        super("AutoGG", Keyboard.KEY_NONE, Category.MISC, "Says GG at game end.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Message", new String[]{"GG","gg","Good Game"}, "GG"));
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (getSetting("Mode").getValue().equals("None")) return;
        String text = event.message.getUnformattedText();
        if (text.contains("Winner") || text.contains("You won") || text.contains("You lost")) {
            mc.thePlayer.sendChatMessage(getSetting("Message").getValue());
        }
    }
}