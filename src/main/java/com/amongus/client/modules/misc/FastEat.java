package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class FastEat extends Module {
    public FastEat() {
        super("FastEat", Keyboard.KEY_NONE, Category.MISC, "FastEat module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
