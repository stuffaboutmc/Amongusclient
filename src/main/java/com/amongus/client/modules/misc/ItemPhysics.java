package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class ItemPhysics extends Module {
    public ItemPhysics() {
        super("ItemPhysics", Keyboard.KEY_NONE, Category.MISC, "ItemPhysics module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
