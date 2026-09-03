package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;
public class ChatBypass extends Module {
    public ChatBypass() {
        super("ChatBypass", Keyboard.KEY_NONE, Category.MISC, "Bypasses chat filters.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
}