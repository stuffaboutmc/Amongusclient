package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AntiSpam extends Module {
    public AntiSpam() {
        super("AntiSpam", Keyboard.KEY_NONE, Category.MISC, "AntiSpam module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
