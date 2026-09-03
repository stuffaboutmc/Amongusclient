package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class PingSpoof extends Module {
    public PingSpoof() {
        super("PingSpoof", Keyboard.KEY_NONE, Category.MISC, "PingSpoof module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
