package myau.client.command.commands;
import myau.client.command.Command;
import myau.client.command.CommandManager;
import myau.client.core.Module;
import myau.client.core.ModuleManager;
public class ToggleCmd extends Command {
    public ToggleCmd() { super("toggle"); }
    public void execute(String[] args) {
        if (args.length < 1) { CommandManager.sendClientMessage("\u00a7cUsage: .toggle <module>"); return; }
        Module m = ModuleManager.getModule(args[0]);
        if (m == null) { CommandManager.sendClientMessage("\u00a7cModule not found: " + args[0]); return; }
        m.toggle();
        CommandManager.sendClientMessage(m.getName() + " \u00a77" + (m.isEnabled() ? "\u00a7aEnabled" : "\u00a7cDisabled"));
    }
}
