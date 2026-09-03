package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class StreamerMode extends Module {
    public StreamerMode() {
        super("StreamerMode", Keyboard.KEY_NONE, Category.MISC, "StreamerMode module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
