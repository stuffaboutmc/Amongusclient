package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class NoSwing extends Module {
    public NoSwing() {
        super("NoSwing", Keyboard.KEY_NONE, Category.MISC, "NoSwing module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
