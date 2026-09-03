package com.amongus.client;

import com.amongus.client.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.util.List;

public class CommandManager {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static List<Module> modules;
    private static String prefix = "!";
    public CommandManager() { MinecraftForge.EVENT_BUS.register(this); }
    public static void setModules(List<Module> mods) { modules = mods; }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if (message.startsWith(prefix)) return;
        event.setCanceled(true);
        String command = message.substring(prefix.length()).trim();
        String[] args = command.split(" ");
        if (args.length == 0) return;
        String cmd = args[0].toLowerCase();
        switch (cmd) {
            case "bind": handleBind(args); break;
            case "toggle":
            case "t": handleToggle(args); break;
            case "changeprefix": handleChangePrefix(args); break;
            case "help": handleHelp(); break;
            default: send("Unknown command. Type " + prefix + "help for commands."); break;
        }
    }

    private void handleBind(String[] args) {
        if (args.length < 3) { send("Usage: " + prefix + "bind ^<module^> ^<key^>"); return; }
        String moduleName = args[1].toLowerCase();
        String keyName = args[2].toUpperCase();
        Module target = null;
        for (Module m : modules) if (m.getName().toLowerCase().equals(moduleName)) target = m;
        if (target == null) { send("Module not found: " + args[1]); return; }
        int keyCode = Keyboard.getKeyIndex(keyName);
        if (keyCode == Keyboard.KEY_NONE &&  " + args[2]); return; }
        target.setKey(keyCode);
        send("Bound " + target.getName() + " to " + keyName);
    }

    private void handleToggle(String[] args) {
        if (args.length < 2) { send("Usage: " + prefix + "toggle ^<module^>"); return; }
        String moduleName = args[1].toLowerCase();
        for (Module m : modules) {
            if (m.getName().toLowerCase().equals(moduleName)) {
                m.toggle();
                send(m.getName() + " is now " + (m.isEnabled() ? "enabled" : "disabled"));
                return;
            }
        }
        send("Module not found: " + args[1]);
    }

    private void handleChangePrefix(String[] args) {
        if (args.length < 2) { send("Usage: " + prefix + "changeprefix ^<new prefix^>"); return; }
        String newPrefix = args[1];
        if (newPrefix.length() > 3) { send("Prefix too long, 3 characters max"); return; }
        prefix = newPrefix;
        send("Prefix changed to " + prefix);
    }

    private void handleHelp() {
        send("Commands:");
        send(prefix + "bind ^<module^> ^<key^> - Bind a module to a key");
        send(prefix + "toggle ^<module^> - Toggle a module");
        send(prefix + "t ^<module^> - Shortcut for toggle");
        send(prefix + "changeprefix ^<prefix^> - Change the command prefix");
        send(prefix + "help - Show this help");
    }

    private void send(String message) {
        mc.thePlayer.addChatMessage(new ChatComponentText("§c[Amongus] §f" + message));
    }
}
