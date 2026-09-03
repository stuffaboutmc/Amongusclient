package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class XRay extends Module {
    public XRay() {
        super("XRay", Keyboard.KEY_NONE, Category.RENDER, "XRay module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
