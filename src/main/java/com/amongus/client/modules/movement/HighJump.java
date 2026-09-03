package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class HighJump extends Module {
    public HighJump() {
        super("HighJump", Keyboard.KEY_NONE, Category.MOVEMENT, "HighJump module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
