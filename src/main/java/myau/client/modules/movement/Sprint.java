package myau.client.module.impl;

import myau.client.module.Module;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Movement");
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
