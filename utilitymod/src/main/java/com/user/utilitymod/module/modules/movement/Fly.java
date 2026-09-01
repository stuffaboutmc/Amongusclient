package com.user.utilitymod.module.modules.movement;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.Setting;
import com.user.utilitymod.util.ReflectionUtil;

public class Fly extends Module {

    private boolean wasAllowFlying;
    private boolean wasFlying;

    public Fly() {
        super("Fly", Category.MOVEMENT);
        addSetting(Setting.slider("Speed", 0.6, 0.1, 2.0, 0.05));
    }

    @Override
    protected void onEnable() {
        if (!isPlayerValid()) return;
        wasAllowFlying = mc.thePlayer.capabilities.allowFlying;
        wasFlying = mc.thePlayer.capabilities.isFlying;
        mc.thePlayer.capabilities.allowFlying = true;
        mc.thePlayer.capabilities.isFlying = true;
    }

    @Override
    protected void onDisable() {
        if (!isPlayerValid()) return;
        mc.thePlayer.capabilities.allowFlying = wasAllowFlying;
        mc.thePlayer.capabilities.isFlying = wasFlying;
        ReflectionUtil.setField(mc.thePlayer.capabilities, "flySpeed", 0.05f);
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;
        float speed = (float) (getSetting("Speed").getValue() * 0.05);
        ReflectionUtil.setField(mc.thePlayer.capabilities, "flySpeed", speed);
    }
}
