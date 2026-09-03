package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;
public class XRay extends Module {
    private float oldGamma;
    public XRay() {
        super("XRay", Keyboard.KEY_NONE, Category.RENDER, "See through blocks.");
        addSetting(new Setting("Mode", new String[]{"None","Ores","Basic","Advanced"}, "Ores"));
    }
    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 10.0F;
        mc.renderGlobal.loadRenderers();
    }
    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
        mc.renderGlobal.loadRenderers();
    }
}