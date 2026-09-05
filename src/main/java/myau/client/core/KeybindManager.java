package com.stuffaboutmc.client.manager;

import com.stuffaboutmc.client.Client;
import com.stuffaboutmc.client.gui.ClickGUI;
import org.lwjgl.input.Keyboard;

public class KeybindManager {

    public void onTick() {
        // RSHIFT toggle clickgui
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            if (ClickGUI.instance != null) {
                ClickGUI.instance.toggleVisibility();
            } else {
                ClickGUI gui = new ClickGUI();
                Minecraft.getMinecraft().displayGuiScreen(gui);
            }
            // prevent rapid toggling
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }

        // module keybinds
        for (Module m : Client.instance.moduleManager.getModules()) {
            if (m.getKeybind() > 0 && Keyboard.isKeyDown(m.getKeybind())) {
                // prevent spam
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
