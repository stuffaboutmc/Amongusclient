package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class LongJump extends Module {
    public LongJump() {
        super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT, "LongJump module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
