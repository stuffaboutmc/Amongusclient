package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoL extends Module {
    public AutoL() {
        super("AutoL", Keyboard.KEY_NONE, Category.MISC, "AutoL module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
