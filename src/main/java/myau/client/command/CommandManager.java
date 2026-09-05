package myau.client.command;

import myau.client.core.Module;
import myau.client.core.ModuleManager;
import myau.client.config.ConfigManager;
import myau.client.gui.ClickGUI;
import myau.client.command.commands.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import java.util.*;

public class CommandManager {
    private static final List<Command> commands = new ArrayList<>();
    private static String prefix = ".";

    public static void init() {
        commands.clear();
        commands.add(new HelpCmd());
        commands.add(new ToggleCmd());
        commands.add(new BindCmd());
        commands.add(new ConfigCmd());
        commands.add(new StyleCmd());
        commands.add(new ModulesCmd());
        commands.add(new CoordsCmd());
    }

    public static boolean handle(String msg) {
        if (msg == null || !msg.startsWith(prefix)) return false;
        String stripped = msg.substring(prefix.length()).trim();
        if (stripped.isEmpty()) return false;
        String[] parts = stripped.split("\\s+");
        String cmdName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(cmdName)) {
                cmd.execute(args);
                return true;
            }
        }
        sendClientMessage("\u00a7cUnknown command: " + cmdName + ". Use " + prefix + "help");
        return true;
    }

    public static String getPrefix() { return prefix; }
    public static void setPrefix(String p) { if (p != null && !p.isEmpty()) prefix = p; }

    public static void sendClientMessage(String msg) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText("\u00a77[\u00a7camong us\u00a77] \u00a7r" + msg));
        }
    }
}
