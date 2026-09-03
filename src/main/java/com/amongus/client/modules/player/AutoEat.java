package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoEat extends Module {
    public AutoEat() {
        super("AutoEat", Keyboard.KEY_NONE, Category.PLAYER, "AutoEat module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
