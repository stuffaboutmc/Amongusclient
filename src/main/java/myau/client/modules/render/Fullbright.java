package myau.client.modules.render;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Fullbright extends Module {
    private float oldGamma = 1.0F;

    public Fullbright() {
        super("Fullbright", "Full brightness gamma", Category.RENDER, Keyboard.KEY_NONE);
    }

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100.0F;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }
}
