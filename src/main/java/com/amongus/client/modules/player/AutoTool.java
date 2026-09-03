package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", Keyboard.KEY_NONE, Category.PLAYER, "AutoTool module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
