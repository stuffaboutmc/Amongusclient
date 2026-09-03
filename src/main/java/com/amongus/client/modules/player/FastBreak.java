package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class FastBreak extends Module {
    public FastBreak() {
        super("FastBreak", Keyboard.KEY_NONE, Category.PLAYER, "FastBreak module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
