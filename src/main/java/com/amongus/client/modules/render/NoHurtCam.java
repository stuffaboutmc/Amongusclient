package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam", Keyboard.KEY_NONE, Category.RENDER, "NoHurtCam module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
