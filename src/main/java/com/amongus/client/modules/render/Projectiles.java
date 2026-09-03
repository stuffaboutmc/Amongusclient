package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Projectiles extends Module {
    public Projectiles() {
        super("Projectiles", Keyboard.KEY_NONE, Category.RENDER, "Projectiles module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
