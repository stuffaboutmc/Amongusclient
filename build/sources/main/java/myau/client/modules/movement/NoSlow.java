package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", "Prevents item use slowdown", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.isUsingItem()) {
            mc.thePlayer.motionX *= 1.0;
            mc.thePlayer.motionZ *= 1.0;
        }
    }
}
