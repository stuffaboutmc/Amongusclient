package myau.client.command.commands;
import myau.client.command.Command;
import myau.client.command.CommandManager;
public class HelpCmd extends Command {
    public HelpCmd() { super("help"); }
    public void execute(String[] args) {
        CommandManager.sendClientMessage("\u00a7eCommands:");
        CommandManager.sendClientMessage("\u00a77. toggle <module> \u00a7- Toggle module");
        CommandManager.sendClientMessage("\u00a77. bind <module> <key> \u00a7- Set keybind");
        CommandManager.sendClientMessage("\u00a77. config <save/load> [name] \u00a7- Config");
        CommandManager.sendClientMessage("\u00a77. style <Rise/Vape/Augustus/Prestige> \u00a7- GUI style");
        CommandManager.sendClientMessage("\u00a77. modules \u00a7- List modules");
        CommandManager.sendClientMessage("\u00a77. coords \u00a7- Show coordinates");
        CommandManager.sendClientMessage("\u00a77. prefix <char> \u00a7- Change prefix");
    }
}
