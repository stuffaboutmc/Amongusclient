package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoArmor extends Module {
    public AutoArmor() {
        super("AutoArmor", Keyboard.KEY_NONE, Category.PLAYER, "AutoArmor module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
