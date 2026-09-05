package com.amongus.client.modules.scaffold;

import com.amongus.client.input.InputSimulator;
import com.amongus.client.modules.Module;
import com.amongus.client.modules.Category;
import java.awt.event.KeyEvent;

public class Scaffold extends Module {
    public enum RotationMode {
        NONE,
        DEFAULT,
        BACKWARDS,
        SIDEWAYS,
        GODBRIDGE,
        SMOOTH,
        SNAP,
        SNAP2,
        HYPIXEL
    }

    public enum TowerMode { NONE, VANILLA, EXTRA, TELLY }
    public enum KeepYMode { NONE, VANILLA, TELLY, TELLY_EXTENDED }

    private RotationMode rotationMode = RotationMode.SMOOTH;
    private TowerMode towerMode = TowerMode.TELLY;
    private KeepYMode keepY = KeepYMode.TELLY;
    private boolean tellySafe = true;
    private int tellyStuckDelay = 4;
    private boolean hypixeltower = true;

    public Scaffold() { super("Scaffold", Category.MOVEMENT); }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null) return;
        if (towerMode == TowerMode.TELLY) {
            boolean moving = mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0;
            if (hypixeltower && !moving && mc.gameSettings.keyBindJump.isKeyDown()) {
                InputSimulator.holdKey(KeyEvent.VK_SPACE, 380);
            }
        }
        if (keepY == KeepYMode.TELLY || keepY == KeepYMode.TELLY_EXTENDED) {
            if (mc.thePlayer.motionY < -0.1) {
                InputSimulator.clickRight();
            }
        }
    }

    @Override public void onRender3D(float pt) {}
    @Override public void onRender() {}
}
