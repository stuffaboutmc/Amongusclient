package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoSoup extends Module {
    public AutoSoup() {
        super("AutoSoup", Keyboard.KEY_NONE, Category.COMBAT, "AutoSoup module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
