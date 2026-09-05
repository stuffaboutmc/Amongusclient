package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {
    private double flySpeed = 2.0;

    public Fly() {
        super("Fly", "Allows creative flying", Category.MOVEMENT, Keyboard.KEY_F);
        addSetting(new Setting("Speed", SettingType.NUMBER, 2.0, 0.5, 10.0));
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.capabilities.isFlying = true;
        }
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        flySpeed = getSetting("Speed").getDouble();
        mc.thePlayer.capabilities.isFlying = true;
        mc.thePlayer.capabilities.setFlySpeed((float)(flySpeed * 0.05));
        mc.thePlayer.noClip = true;
        mc.thePlayer.motionY = 0;
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY += flySpeed * 0.5;
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.thePlayer.motionY -= flySpeed * 0.5;
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.capabilities.isFlying = false;
            mc.thePlayer.capabilities.setFlySpeed(0.05F);
            mc.thePlayer.noClip = false;
        }
    }
}
