package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoPot extends Module {
    public AutoPot() {
        super("AutoPot", Keyboard.KEY_NONE, Category.COMBAT, "AutoPot module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
