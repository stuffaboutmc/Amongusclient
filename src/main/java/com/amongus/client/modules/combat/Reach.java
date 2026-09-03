package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {
    public Reach() {
        super("Reach", Keyboard.KEY_NONE, Category.COMBAT, "Reach module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
