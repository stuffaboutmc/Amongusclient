package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class BlockOverlay extends Module {
    public BlockOverlay() {
        super("BlockOverlay", Keyboard.KEY_NONE, Category.RENDER, "BlockOverlay module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
