package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", Keyboard.KEY_NONE, Category.PLAYER, "FastPlace module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
