package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Crits extends Module {
    public Crits() {
        super("Crits", Keyboard.KEY_NONE, Category.COMBAT, "Crits module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
