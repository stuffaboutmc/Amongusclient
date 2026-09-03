package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Breadcrumbs extends Module {
    public Breadcrumbs() {
        super("Breadcrumbs", Keyboard.KEY_NONE, Category.RENDER, "Breadcrumbs module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
