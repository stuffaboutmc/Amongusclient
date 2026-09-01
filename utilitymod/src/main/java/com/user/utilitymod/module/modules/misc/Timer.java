package com.user.utilitymod.module.modules.misc;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.Setting;

public class Timer extends Module {

    private float originalTickLength;

    public Timer() {
        super("Timer", Category.MISC);
        addSetting(Setting.slider("Speed", 1.0, 0.1, 4.0, 0.1));
    }

    @Override
    protected void onEnable() {
        if (mc.timer != null) originalTickLength = mc.timer.timerSpeed;
    }

    @Override
    public void onTick() {
        if (mc.timer != null) {
            mc.timer.timerSpeed = (float) getSetting("Speed").getValue();
        }
    }

    @Override
    protected void onDisable() {
        if (mc.timer != null) mc.timer.timerSpeed = originalTickLength <= 0 ? 1.0f : originalTickLength;
    }
}
