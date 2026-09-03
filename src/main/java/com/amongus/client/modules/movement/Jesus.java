package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", Keyboard.KEY_NONE, Category.MOVEMENT, "Jesus module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
