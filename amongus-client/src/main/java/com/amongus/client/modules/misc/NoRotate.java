package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;
public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", Keyboard.KEY_NONE, Category.MISC, "Server can't rotate you.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
}