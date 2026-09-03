package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class FakeLag extends Module {
    public FakeLag() {
        super("FakeLag", Keyboard.KEY_NONE, Category.PLAYER, "FakeLag module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
