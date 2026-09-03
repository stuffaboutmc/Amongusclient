package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class NoHitDelay extends Module {
    public NoHitDelay() {
        super("NoHitDelay", Keyboard.KEY_NONE, Category.COMBAT, "NoHitDelay module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
