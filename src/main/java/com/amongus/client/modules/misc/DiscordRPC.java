package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class DiscordRPC extends Module {
    public DiscordRPC() {
        super("DiscordRPC", Keyboard.KEY_NONE, Category.MISC, "DiscordRPC module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
