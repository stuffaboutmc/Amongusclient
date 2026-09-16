package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {

    public Speed() {
        super("Speed", "Increases movement speed", Category.MOVEMENT, Keyboard.KEY_NONE);
        addSetting(new Setting("Mode", SettingType.MODE, "BHop", "BHop", "Strafe", "NCP"));
        addSetting(new Setting("Speed", SettingType.NUMBER, 1.2, 0.5, 3.0));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.onGround) {
            // speed logic here
        }
    }
}
