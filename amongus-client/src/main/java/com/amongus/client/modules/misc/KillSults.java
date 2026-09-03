package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class KillSults extends Module {
    public KillSults() {
        super("KillSults", Keyboard.KEY_NONE, Category.MISC, "Sends insults on kill.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (getSetting("Mode").getValue().equals("None")) return;
        String text = event.message.getUnformattedText();
        if (text.contains("by " + mc.thePlayer.getName())) {
            mc.thePlayer.sendChatMessage("ez");
        }
    }
}