package myau.client.core;

import myau.client.module.Module;
import myau.client.gui.ClickGUI;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class KeybindManager {

    private static long lastToggle = 0;

    public static void init() {}

    public static void update() {
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            long now = System.currentTimeMillis();
            if (now - lastToggle > 200) {
                lastToggle = now;
                if (Minecraft.getMinecraft().currentScreen instanceof ClickGUI) {
                    Minecraft.getMinecraft().displayGuiScreen(null);
                } else {
                    Minecraft.getMinecraft().displayGuiScreen(new ClickGUI());
                }
            }
        }

        for (Module m : ModuleManager.getModules()) {
            if (m.getKeybind() > 0 && Keyboard.isKeyDown(m.getKeybind())) {
                if (!m.wasPressed) {
                    m.toggle();
                    m.wasPressed = true;
                }
            } else {
                m.wasPressed = false;
            }
        }
    }

    public static void setKeybind(Module m, int key) {
        if (m != null) m.setKeybind(key);
    }
}
