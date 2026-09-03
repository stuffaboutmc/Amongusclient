package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class NoVoid extends Module {
    public NoVoid() {
        super("NoVoid", Keyboard.KEY_NONE, Category.PLAYER, "NoVoid module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
