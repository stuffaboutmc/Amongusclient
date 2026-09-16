package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Step extends Module {
    private double height = 2.0;

    public Step() {
        super("Step", "Steps up blocks instantly", Category.MOVEMENT, Keyboard.KEY_NONE);
        addSetting(new Setting("Height", SettingType.NUMBER, 2.0, 1.0, 5.0));
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.stepHeight = (float) getSetting("Height").getDouble();
        }
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        mc.thePlayer.stepHeight = (float) getSetting("Height").getDouble();
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.stepHeight = 0.6F;
        }
    }
}
