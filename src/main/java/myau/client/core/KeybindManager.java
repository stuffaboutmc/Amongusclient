package com.stuffaboutmc.client.manager;

import com.stuffaboutmc.client.Client;
import com.stuffaboutmc.client.gui.ClickGUI;
import com.stuffaboutmc.client.module.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class KeybindManager {

    private long lastToggle = 0;

    public void onTick() {
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            long now = System.currentTimeMillis();
            if (now - lastToggle > 200) {
                lastToggle = now;
                ClickGUI gui = new ClickGUI();
                if (Minecraft.getMinecraft().currentScreen == gui) {
                    Minecraft.getMinecraft().displayGuiScreen(null);
                } else {
                    Minecraft.getMinecraft().displayGuiScreen(gui);
                }
            }
        }

        for (Module m : Client.instance.moduleManager.getModules()) {
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
}
