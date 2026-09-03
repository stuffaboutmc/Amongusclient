package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class CommandSpy extends Module {
    public CommandSpy() {
        super("CommandSpy", Keyboard.KEY_NONE, Category.MISC, "Shows other players' commands.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (getSetting("Mode").getValue().equals("None")) return;
        String text = event.message.getUnformattedText();
        if (text.startsWith("/")) {
            mc.thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("§7[Spy] §f" + text));
        }
    }
}