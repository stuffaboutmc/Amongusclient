package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Blink extends Module {
    public Blink() {
        super("Blink", Keyboard.KEY_NONE, Category.MOVEMENT, "Blink module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
