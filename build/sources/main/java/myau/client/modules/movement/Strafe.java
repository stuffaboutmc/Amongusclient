package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Strafe extends Module {
    private double speed = 0.3;

    public Strafe() {
        super("Strafe", "Custom strafe movement", Category.MOVEMENT, Keyboard.KEY_NONE);
        addSetting(new Setting("Speed", SettingType.NUMBER, 0.3, 0.1, 2.0));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0) return;

        float yaw = mc.thePlayer.rotationYaw;
        float forward = mc.thePlayer.moveForward;
        float strafe = mc.thePlayer.moveStrafing;

        double s = getSetting("Speed").getDouble();
        double mx = 0, mz = 0;

        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0) ? -45 : 45;
            } else if (strafe < 0) {
                yaw += (forward > 0) ? 45 : -45;
            }
            strafe = 0;
            if (forward > 0) forward = 1;
            else if (forward < 0) forward = -1;
        }

        mx = -Math.sin(Math.toRadians(yaw)) * forward * s;
        mz = Math.cos(Math.toRadians(yaw)) * forward * s;

        mc.thePlayer.motionX = mx;
        mc.thePlayer.motionZ = mz;
    }
}
