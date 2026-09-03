package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;
public class LagRange extends Module {
    public LagRange() {
        super("LagRange", Keyboard.KEY_NONE, Category.MISC, "Latency-based range.");
        addSetting(new Setting("Mode", new String[]{"None","Simple","Advanced"}, "Simple"));
        addSetting(new Setting("Latency", 50, 500, 150, 10));
        addSetting(new Setting("RangeBoost", 0, 3, 1, 0.1));
    }
}