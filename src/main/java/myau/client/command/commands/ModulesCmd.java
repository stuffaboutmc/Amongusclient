package myau.client.command.commands;
import myau.client.command.Command;
import myau.client.command.CommandManager;
import myau.client.core.Module;
import myau.client.core.ModuleManager;
public class ModulesCmd extends Command {
    public ModulesCmd() { super("modules"); }
    public void execute(String[] args) {
        CommandManager.sendClientMessage("\u00a7eModules (" + ModuleManager.getModules().size() + "):");
        StringBuilder sb = new StringBuilder();
        for (Module m : ModuleManager.getModules()) {
            String s = (m.isEnabled() ? "\u00a7a" : "\u00a7c") + m.getName() + "\u00a77, ";
            if (sb.length() + s.length() > 80) { CommandManager.sendClientMessage(sb.toString()); sb = new StringBuilder(); }
            sb.append(s);
        }
        if (sb.length() > 0) CommandManager.sendClientMessage(sb.toString());
    }
}
