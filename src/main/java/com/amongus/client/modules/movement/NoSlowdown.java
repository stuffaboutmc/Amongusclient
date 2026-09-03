package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class NoSlowdown extends Module {
    public NoSlowdown() {
        super("NoSlowdown", Keyboard.KEY_NONE, Category.MOVEMENT, "NoSlowdown module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
