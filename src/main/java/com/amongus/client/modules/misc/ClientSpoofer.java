package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class ClientSpoofer extends Module {
    public ClientSpoofer() {
        super("ClientSpoofer", Keyboard.KEY_NONE, Category.MISC, "ClientSpoofer module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
