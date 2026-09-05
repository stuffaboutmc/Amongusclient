package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    private double speedMultiplier = 1.5;

    public Speed() {
        super("Speed", "Increases movement speed", Category.MOVEMENT, Keyboard.KEY_V);
        addSetting(new Setting("Multiplier", SettingType.NUMBER, 1.5, 1.0, 5.0));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
            float yaw = mc.thePlayer.rotationYaw;
            double mx = -Math.sin(Math.toRadians(yaw)) * 0.26 * speedMultiplier;
            double mz = Math.cos(Math.toRadians(yaw)) * 0.26 * speedMultiplier;
            mc.thePlayer.motionX = mx;
            mc.thePlayer.motionZ = mz;
        }
    }
}
