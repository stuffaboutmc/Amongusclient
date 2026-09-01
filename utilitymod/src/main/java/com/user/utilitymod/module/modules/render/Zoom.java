package com.user.utilitymod.module.modules.render;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.Setting;
import org.lwjgl.input.Keyboard;

public class Zoom extends Module {

    private double originalFov;
    private boolean zooming = false;

    public Zoom() {
        super("Zoom", Category.RENDER, Keyboard.KEY_C);
        addSetting(Setting.slider("Amount", 4.0, 1.5, 10.0, 0.5));
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;

        boolean held = Keyboard.isKeyDown(getKeybind());
        if (held && !zooming) {
            zooming = true;
            originalFov = mc.gameSettings.fovSetting;
        } else if (!held && zooming) {
            zooming = false;
            mc.gameSettings.fovSetting = (float) originalFov;
        }

        if (zooming) {
            mc.gameSettings.fovSetting = (float) (originalFov / getSetting("Amount").getValue());
        }
    }

    @Override
    protected void onDisable() {
        if (zooming) {
            mc.gameSettings.fovSetting = (float) originalFov;
            zooming = false;
        }
    }
}
