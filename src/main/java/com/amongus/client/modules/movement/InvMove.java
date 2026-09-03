package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class InvMove extends Module {
    public InvMove() {
        super("InvMove", Keyboard.KEY_NONE, Category.MOVEMENT, "InvMove module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
