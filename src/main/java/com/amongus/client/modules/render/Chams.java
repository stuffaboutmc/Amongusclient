package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Chams extends Module {
    public Chams() {
        super("Chams", Keyboard.KEY_NONE, Category.RENDER, "Chams module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
