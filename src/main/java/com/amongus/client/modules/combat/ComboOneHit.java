package com.amongus.client.modules.combat;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class ComboOneHit extends Module {
    public ComboOneHit() {
        super("ComboOneHit", Keyboard.KEY_NONE, Category.COMBAT, "ComboOneHit module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
