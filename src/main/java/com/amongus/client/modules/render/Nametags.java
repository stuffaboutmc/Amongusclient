package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Nametags extends Module {
    public Nametags() {
        super("Nametags", Keyboard.KEY_NONE, Category.RENDER, "Nametags module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
