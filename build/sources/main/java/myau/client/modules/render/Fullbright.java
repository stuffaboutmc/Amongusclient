package myau.client.modules.render;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Fullbright extends Module {

    private float oldGamma;

    public Fullbright() {
        super("FullBright", "Maxes out gamma for full brightness", Category.RENDER, Keyboard.KEY_NONE);
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
