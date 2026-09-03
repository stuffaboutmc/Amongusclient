package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Regen extends Module {
    public Regen() {
        super("Regen", Keyboard.KEY_NONE, Category.PLAYER, "Regen module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
