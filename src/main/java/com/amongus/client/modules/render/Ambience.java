package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Ambience extends Module {
    public Ambience() {
        super("Ambience", Keyboard.KEY_NONE, Category.RENDER, "Ambience module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
