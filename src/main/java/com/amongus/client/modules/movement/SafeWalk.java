package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", Keyboard.KEY_NONE, Category.MOVEMENT, "SafeWalk module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
