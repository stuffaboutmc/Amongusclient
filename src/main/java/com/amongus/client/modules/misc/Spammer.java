package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Spammer extends Module {
    public Spammer() {
        super("Spammer", Keyboard.KEY_NONE, Category.MISC, "Spammer module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
