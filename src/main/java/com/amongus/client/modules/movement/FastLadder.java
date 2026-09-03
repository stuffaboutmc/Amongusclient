package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class FastLadder extends Module {
    public FastLadder() {
        super("FastLadder", Keyboard.KEY_NONE, Category.MOVEMENT, "FastLadder module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
