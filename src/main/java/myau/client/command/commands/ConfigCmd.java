package myau.client.command.commands;
import myau.client.command.Command;
import myau.client.command.CommandManager;
import myau.client.config.ConfigManager;
public class ConfigCmd extends Command {
    public ConfigCmd() { super("config"); }
    public void execute(String[] args) {
        if (args.length < 1) { CommandManager.sendClientMessage("\u00a7cUsage: .config <save/load> [name]"); return; }
        String name = args.length > 1 ? args[1] : "default";
        if (args[0].equalsIgnoreCase("save")) { ConfigManager.save(name); CommandManager.sendClientMessage("\u00a7aSaved config: " + name); }
        else if (args[0].equalsIgnoreCase("load")) { ConfigManager.load(name); CommandManager.sendClientMessage("\u00a7aLoaded config: " + name); }
        else CommandManager.sendClientMessage("\u00a7cUsage: .config <save/load> [name]");
    }
}
