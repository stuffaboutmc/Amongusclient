package com.amongus.client.modules.player;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AutoHeal extends Module {
    public AutoHeal() {
        super("AutoHeal", Keyboard.KEY_NONE, Category.PLAYER, "AutoHeal module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
