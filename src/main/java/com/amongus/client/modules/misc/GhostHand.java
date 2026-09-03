package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class GhostHand extends Module {
    public GhostHand() {
        super("GhostHand", Keyboard.KEY_NONE, Category.MISC, "GhostHand module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
