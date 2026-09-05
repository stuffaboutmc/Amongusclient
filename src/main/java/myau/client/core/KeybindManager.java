package myau.client.core;

import myau.client.module.Module;
import org.lwjgl.input.Keyboard;

public class KeybindManager {
    public static void init() {}
    public static void update() {
        for (Module m : ModuleManager.getModules()) {
            if (m.getKeybind() > 0 && Keyboard.isKeyDown(m.getKeybind())) {
                if (!m.wasPressed) { m.toggle(); m.wasPressed = true; }
            } else {
                m.wasPressed = false;
            }
        }
    }
    public static void setKeybind(Module m, int key) { if (m != null) m.setKeybind(key); }
}
