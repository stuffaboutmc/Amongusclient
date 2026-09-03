package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;
public class Reach extends Module {
    public Reach() {
        super("Reach", Keyboard.KEY_NONE, Category.COMBAT, "Extends your attack range.");
        addSetting(new Setting("Range", 3, 8, 6, 0.5));
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("OnlySword", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("ThroughWalls", new String[]{"Off","On"}, "Off"));
    }
}