package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class SpinBot extends Module {
    public SpinBot() {
        super("SpinBot", Keyboard.KEY_NONE, Category.MISC, "SpinBot module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
