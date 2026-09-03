package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.util.HashMap;
import java.util.Map;
public class AntiSpam extends Module {
    private Map<String, Long> lastMessages = new HashMap<>();
    public AntiSpam() {
        super("AntiSpam", Keyboard.KEY_NONE, Category.MISC, "Blocks repeated messages.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Threshold", 1000, 10000, 3000, 500));
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (getSetting("Mode").getValue().equals("None")) return;
        String text = event.message.getUnformattedText();
        Long last = lastMessages.get(text);
        if (last != null && System.currentTimeMillis() - last < (int) getSetting("Threshold").getDoubleValue()) {
            event.setCanceled(true);
        }
        lastMessages.put(text, System.currentTimeMillis());
    }
}