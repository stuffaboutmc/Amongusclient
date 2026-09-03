package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoDisconnect extends Module {
    public AutoDisconnect() {
        super("AutoDisconnect", Keyboard.KEY_NONE, Category.PLAYER, "AutoDisconnect module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
