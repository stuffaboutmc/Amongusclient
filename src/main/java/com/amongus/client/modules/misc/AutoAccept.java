package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoAccept extends Module {
    public AutoAccept() {
        super("AutoAccept", Keyboard.KEY_NONE, Category.MISC, "AutoAccept module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
