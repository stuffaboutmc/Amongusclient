package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Derp extends Module {
    public Derp() {
        super("Derp", Keyboard.KEY_NONE, Category.MISC, "Derp module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
