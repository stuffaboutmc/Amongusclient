package com.user.utilitymod.module.modules.render;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

public class Fullbright extends Module {

    private float originalGamma;

    public Fullbright() {
        super("Fullbright", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        originalGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 1000.0f;
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.gammaSetting = originalGamma;
    }
}
