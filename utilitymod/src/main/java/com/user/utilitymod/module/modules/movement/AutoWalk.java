package com.user.utilitymod.module.modules.movement;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

public class AutoWalk extends Module {

    public AutoWalk() {
        super("AutoWalk", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;
        mc.gameSettings.keyBindForward.pressed = true;
    }

    @Override
    protected void onDisable() {
        if (mc.gameSettings != null) {
            mc.gameSettings.keyBindForward.pressed = false;
        }
    }
}
