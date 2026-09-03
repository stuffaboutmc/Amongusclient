package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoWalk extends Module {
    public AutoWalk() {
        super("AutoWalk", Keyboard.KEY_NONE, Category.MISC, "AutoWalk module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
