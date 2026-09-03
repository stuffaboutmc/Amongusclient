package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AntiAFK extends Module {
    public AntiAFK() {
        super("AntiAFK", Keyboard.KEY_NONE, Category.PLAYER, "AntiAFK module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
