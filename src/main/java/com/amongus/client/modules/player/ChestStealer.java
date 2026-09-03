package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {
    public ChestStealer() {
        super("ChestStealer", Keyboard.KEY_NONE, Category.PLAYER, "ChestStealer module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
