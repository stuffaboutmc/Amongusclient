package com.amongus.client;

import com.amongus.client.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

public class CommandManager {

    public static boolean handleCommand(String msg) {
        if (!msg.startsWith(".")) return false;
        String[] parts = msg.substring(1).split(" ");
        if (parts.length == 0) return false;
        String cmd = parts[0].toLowerCase();
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);
        switch (cmd) {
            case "toggle":
            case "t":
                handleToggle(args);
                return true;
            case "bind":
                handleBind(args);
                return true;
            case "help":
                sendHelp();
                return true;
            default:
                sendMessage("Unknown command. Type .help for commands.");
                return true;
        }
    }

    private static void handleToggle(String[] args) {
        if (args.length < 1) {
            sendMessage("Usage: .toggle <module>  (or .t <module>)");
            return;
        }
        String name = args[0];
        for (Module mod : ModuleManager.modules) {
            if (mod.getName().equalsIgnoreCase(name)) {
                mod.toggle();
                sendMessage(EnumChatFormatting.GREEN + mod.getName() + " is now " + (mod.isEnabled() ? "enabled" : "disabled"));
                return;
            }
        }
        sendMessage(EnumChatFormatting.RED + "Module \"" + name + "\" not found.");
    }

    private static void handleBind(String[] args) {
        if (args.length < 2) {
            sendMessage("Usage: .bind <module> <key>");
            return;
        }
        String name = args[0];
        String keyName = args[1];
        int keyCode = Keyboard.getKeyIndex(keyName.toUpperCase());
        if (keyCode == Keyboard.KEY_NONE) {
            sendMessage(EnumChatFormatting.RED + "Invalid key: " + keyName);
            return;
        }
        for (Module mod : ModuleManager.modules) {
            if (mod.getName().equalsIgnoreCase(name)) {
                mod.setKeyBind(keyCode);
                sendMessage(EnumChatFormatting.GREEN + mod.getName() + " bound to " + keyName);
                return;
            }
        }
        sendMessage(EnumChatFormatting.RED + "Module \"" + name + "\" not found.");
    }

    private static void sendHelp() {
        sendMessage(EnumChatFormatting.YELLOW + "=== Commands ===");
        sendMessage(".toggle <module>  or .t <module>  - toggle module");
        sendMessage(".bind <module> <key> - bind module to a key (e.g., .bind killaura R)");
        sendMessage(".help - show this help");
        sendMessage(EnumChatFormatting.GRAY + "Note: key names are as in Minecraft (e.g., R, RIGHT_SHIFT, LCONTROL)");
    }

    private static void sendMessage(String text) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(text));
    }
}
