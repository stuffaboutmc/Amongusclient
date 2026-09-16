package myau.client.command.commands;
import myau.client.command.Command;
import myau.client.command.CommandManager;
import myau.client.core.KeybindManager;
import myau.client.core.Module;
import myau.client.core.ModuleManager;
import org.lwjgl.input.Keyboard;
public class BindCmd extends Command {
    public BindCmd() { super("bind"); }
    public void execute(String[] args) {
        if (args.length < 2) { CommandManager.sendClientMessage("\u00a7cUsage: .bind <module> <key>"); return; }
        Module m = ModuleManager.getModule(args[0]);
        if (m == null) { CommandManager.sendClientMessage("\u00a7cModule not found: " + args[0]); return; }
        int key = Keyboard.getKeyIndex(args[1].toUpperCase());
        KeybindManager.setKeybind(m, key);
        CommandManager.sendClientMessage("\u00a7aBound " + m.getName() + " to " + Keyboard.getKeyName(key));
    }
}
