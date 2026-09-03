package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Headless extends Module {
    public Headless() {
        super("Headless", Keyboard.KEY_NONE, Category.MISC, "Headless module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
