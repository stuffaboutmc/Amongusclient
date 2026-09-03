package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class CustomFOV extends Module {
    public CustomFOV() {
        super("CustomFOV", Keyboard.KEY_NONE, Category.RENDER, "CustomFOV module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
