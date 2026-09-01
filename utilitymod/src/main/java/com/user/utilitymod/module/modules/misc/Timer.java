package com.user.utilitymod.module.modules.misc;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.Setting;
import com.user.utilitymod.util.ReflectionUtil;

public class Timer extends Module {

    private float originalTickLength = 1.0f;

    public Timer() {
        super("Timer", Category.MISC);
        addSetting(Setting.slider("Speed", 1.0, 0.1, 4.0, 0.1));
    }

    private net.minecraft.util.Timer getTimer() {
        Object timer = ReflectionUtil.getField(mc, "timer");
        return timer instanceof net.minecraft.util.Timer ? (net.minecraft.util.Timer) timer : null;
    }

    @Override
    protected void onEnable() {
        net.minecraft.util.Timer t = getTimer();
        if (t != null) originalTickLength = t.timerSpeed;
    }

    @Override
    public void onTick() {
        net.minecraft.util.Timer t = getTimer();
        if (t != null) {
            t.timerSpeed = (float) getSetting("Speed").getValue();
        }
    }

    @Override
    protected void onDisable() {
        net.minecraft.util.Timer t = getTimer();
        if (t != null) t.timerSpeed = originalTickLength <= 0 ? 1.0f : originalTickLength;
    }
}
