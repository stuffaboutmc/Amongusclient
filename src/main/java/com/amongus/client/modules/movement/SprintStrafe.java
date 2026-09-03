package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class SprintStrafe extends Module {
    public SprintStrafe() {
        super("SprintStrafe", Keyboard.KEY_NONE, Category.MOVEMENT, "SprintStrafe module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
