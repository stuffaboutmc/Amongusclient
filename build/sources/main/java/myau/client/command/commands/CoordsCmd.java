package myau.client.command.commands;
import myau.client.command.Command;
import myau.client.command.CommandManager;
import net.minecraft.client.Minecraft;
public class CoordsCmd extends Command {
    public CoordsCmd() { super("coords"); }
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) { CommandManager.sendClientMessage("\u00a7cNot in game"); return; }
        int x = (int)mc.thePlayer.posX, y = (int)mc.thePlayer.posY, z = (int)mc.thePlayer.posZ;
        CommandManager.sendClientMessage("\u00a7eXYZ: \u00a77" + x + " / " + y + " / " + z);
    }
}
