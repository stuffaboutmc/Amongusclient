package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class MemoryFix extends Module {
    public MemoryFix() {
        super("MemoryFix", Keyboard.KEY_NONE, Category.MISC, "MemoryFix module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
