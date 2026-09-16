package myau.client.modules.player;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FastPlace extends Module {
    private int delay = 0;

    public FastPlace() {
        super("FastPlace", "Removes right-click delay", Category.PLAYER, Keyboard.KEY_NONE);
        addSetting(new Setting("Delay", SettingType.NUMBER, 0, 0, 4));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        try {
            java.lang.reflect.Field f = Minecraft.class.getDeclaredField("rightClickDelayTimer");
            f.setAccessible(true);
            f.setInt(mc, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            try {
                java.lang.reflect.Field f = Minecraft.class.getDeclaredField("rightClickDelayTimer");
                f.setAccessible(true);
                f.setInt(mc, 4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
