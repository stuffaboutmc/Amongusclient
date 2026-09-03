package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoGG extends Module {
    public AutoGG() {
        super("AutoGG", Keyboard.KEY_NONE, Category.MISC, "AutoGG module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
