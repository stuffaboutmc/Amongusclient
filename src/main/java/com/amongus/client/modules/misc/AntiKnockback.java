package com.amongus.client.modules.misc;

import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;

public class AntiKnockback extends Module {
    public AntiKnockback() {
        super("AntiKnockback", Keyboard.KEY_NONE, Category.MISC, "AntiKnockback module. Augustus-style.");
        addSetting(new Setting("Mode", new String[]{"None", "Basic", "Advanced"}, "Basic"));
    }
}
