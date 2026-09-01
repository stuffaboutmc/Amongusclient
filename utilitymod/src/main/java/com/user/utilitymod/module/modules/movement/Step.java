package com.user.utilitymod.module.modules.movement;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.Setting;

public class Step extends Module {

    private float originalStepHeight;

    public Step() {
        super("Step", Category.MOVEMENT);
        addSetting(Setting.slider("Height", 1.0, 0.5, 1.5, 0.1));
    }

    @Override
    protected void onEnable() {
        if (!isPlayerValid()) return;
        originalStepHeight = mc.thePlayer.stepHeight;
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;
        mc.thePlayer.stepHeight = (float) (0.6 + getSetting("Height").getValue());
    }

    @Override
    protected void onDisable() {
        if (!isPlayerValid()) return;
        mc.thePlayer.stepHeight = originalStepHeight;
    }
}
