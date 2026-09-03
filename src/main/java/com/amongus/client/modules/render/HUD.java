package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class HUD extends Module {
    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.RENDER, "HUD module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
