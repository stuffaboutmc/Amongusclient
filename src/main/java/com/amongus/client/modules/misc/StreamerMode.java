package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class StreamerMode extends Module {
    public StreamerMode() {
        super("StreamerMode", Keyboard.KEY_NONE, Category.MISC, "Hides sensitive info.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (getSetting("Mode").getValue().equals("None")) return;
        String text = event.message.getUnformattedText();
        if (text.contains(mc.thePlayer.getName())) {
            event.message = new net.minecraft.util.ChatComponentText(text.replace(mc.thePlayer.getName(), "You"));
        }
    }
}