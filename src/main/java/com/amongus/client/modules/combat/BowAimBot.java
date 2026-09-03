package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class BowAimBot extends Module {
    public BowAimBot() {
        super("BowAimBot", Keyboard.KEY_NONE, Category.COMBAT, "BowAimBot module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
