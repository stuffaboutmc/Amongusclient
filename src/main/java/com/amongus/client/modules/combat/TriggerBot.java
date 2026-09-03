package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class TriggerBot extends Module {
    public TriggerBot() {
        super("TriggerBot", Keyboard.KEY_NONE, Category.COMBAT, "TriggerBot module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
