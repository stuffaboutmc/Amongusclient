package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class InventoryCleaner extends Module {
    public InventoryCleaner() {
        super("InventoryCleaner", Keyboard.KEY_NONE, Category.PLAYER, "InventoryCleaner module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
