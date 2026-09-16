package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Automatically sprints", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
