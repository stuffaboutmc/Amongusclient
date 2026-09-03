package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;
public class ReachPlus extends Module {
    public ReachPlus() {
        super("ReachPlus", Keyboard.KEY_NONE, Category.COMBAT, "Extended reach.");
        addSetting(new Setting("Range", 4, 10, 7, 0.5));
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
}