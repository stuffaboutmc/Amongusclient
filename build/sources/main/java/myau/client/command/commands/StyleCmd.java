package myau.client.command.commands;
import myau.client.command.Command;
import myau.client.command.CommandManager;
import myau.client.gui.ClickGUI;
import myau.client.gui.GuiStyle;
public class StyleCmd extends Command {
    public StyleCmd() { super("style"); }
    public void execute(String[] args) {
        if (args.length < 1) { CommandManager.sendClientMessage("\u00a7cUsage: .style <Rise/Vape/Augustus/Prestige>"); return; }
        try {
            GuiStyle s = GuiStyle.valueOf(args[0]);
            ClickGUI.getInstance().setStyle(s);
            CommandManager.sendClientMessage("\u00a7aStyle set to " + s.name());
        } catch (Exception e) { CommandManager.sendClientMessage("\u00a7cInvalid style. Use: Rise, Vape, Augustus, Prestige"); }
    }
}
