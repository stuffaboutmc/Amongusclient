package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class FastUse extends Module {
    public FastUse() {
        super("FastUse", Keyboard.KEY_NONE, Category.PLAYER, "FastUse module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
