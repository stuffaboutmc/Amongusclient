package myau.client.modules.combat;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    private double horizontal = 0.0;
    private double vertical = 0.0;

    public Velocity() {
        super("Velocity", "Reduces knockback from attacks", Category.COMBAT, Keyboard.KEY_NONE);
        addSetting(new Setting("Horizontal", SettingType.NUMBER, 0.0, 0.0, 100.0));
        addSetting(new Setting("Vertical", SettingType.NUMBER, 0.0, 0.0, 100.0));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.hurtTime > 0) {
            mc.thePlayer.motionX *= horizontal / 100.0;
            mc.thePlayer.motionZ *= horizontal / 100.0;
            mc.thePlayer.motionY *= vertical / 100.0;
        }
    }
}
