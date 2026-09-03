package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Freecam extends Module {
    public Freecam() {
        super("Freecam", Keyboard.KEY_NONE, Category.RENDER, "Freecam module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
