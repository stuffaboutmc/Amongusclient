package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoClicker extends Module {
    public AutoClicker() {
        super("AutoClicker", Keyboard.KEY_NONE, Category.COMBAT, "AutoClicker module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
