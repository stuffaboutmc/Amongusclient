package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class DeathCoords extends Module {
    public DeathCoords() {
        super("DeathCoords", Keyboard.KEY_NONE, Category.MISC, "Shows death coordinates.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (getSetting("Mode").getValue().equals("None")) return;
        String text = event.message.getUnformattedText();
        if (text.contains("died") || text.contains("killed")) {
            mc.thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText("§cDeath at X:" + (int)mc.thePlayer.posX + " Y:" + (int)mc.thePlayer.posY + " Z:" + (int)mc.thePlayer.posZ));
        }
    }
}