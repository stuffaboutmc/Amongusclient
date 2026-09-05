package myau.client.module.impl;

import myau.client.module.Module;

public class FullBright extends Module {

    private float oldGamma;

    public FullBright() {
        super("FullBright", "Visual");
    }

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100f;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }
}
