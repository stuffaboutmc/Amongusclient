package myau.client.core;

import java.util.*;
import org.lwjgl.input.Keyboard;

public class KeybindManager {
    private static final int GUI_KEY = Keyboard.KEY_RSHIFT;
    private static final Map<Integer, Boolean> prevStates = new HashMap<>();

    public static void init() { prevStates.clear(); }

    public static void update() {
        for (Module m : ModuleManager.getModules()) {
            int key = m.getKey();
            if (key == 0 || key == GUI_KEY) continue;
            boolean down = Keyboard.isKeyDown(key);
            Boolean was = prevStates.get(key);
            if (was == null) was = false;
            if (down && !was) m.toggle();
            prevStates.put(key, down);
        }
    }

    public static void setKeybind(Module m, int key) {
        if (m == null || key == GUI_KEY) return;
        for (Module other : ModuleManager.getModules()) {
            if (other != m && other.getKey() == key && key != 0) other.setKey(0);
        }
        m.setKey(key);
    }

    public static String getKeyName(int key) {
        if (key == 0) return "None";
        return Keyboard.getKeyName(key);
    }
}
