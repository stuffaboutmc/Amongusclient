package myau.client.modules.player;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FastBreak extends Module {
    private double multiplier = 1.5;

    public FastBreak() {
        super("FastBreak", "Break blocks faster", Category.PLAYER, Keyboard.KEY_NONE);
        addSetting(new Setting("Multiplier", SettingType.NUMBER, 1.5, 1.0, 5.0));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        try {
            java.lang.reflect.Field f = Minecraft.class.getDeclaredField("playerController");
            f.setAccessible(true);
            Object controller = f.get(mc);
            java.lang.reflect.Field delayField = controller.getClass().getDeclaredField("blockHitDelay");
            delayField.setAccessible(true);
            delayField.setInt(controller, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
