package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class NoWeb extends Module {
    public NoWeb() {
        super("NoWeb", Keyboard.KEY_NONE, Category.MISC, "NoWeb module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
