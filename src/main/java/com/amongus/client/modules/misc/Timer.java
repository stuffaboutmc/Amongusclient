package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Timer extends Module {
    public Timer() {
        super("Timer", Keyboard.KEY_NONE, Category.MISC, "Timer module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
