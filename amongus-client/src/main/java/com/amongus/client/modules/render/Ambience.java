package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import org.lwjgl.input.Keyboard;
public class Ambience extends Module {
    public Ambience() {
        super("Ambience", Keyboard.KEY_NONE, Category.RENDER, "Changes world ambiance.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Time", 0, 24000, 6000, 100));
    }
    @Override
    public void onEnable() {
        mc.theWorld.setWorldTime((long) getSetting("Time").getDoubleValue());
    }
}