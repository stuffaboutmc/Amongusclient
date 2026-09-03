package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Phase extends Module {
    public Phase() {
        super("Phase", Keyboard.KEY_NONE, Category.MOVEMENT, "Phase module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
