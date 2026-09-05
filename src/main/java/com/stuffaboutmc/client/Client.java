package com.stuffaboutmc.client;

import com.stuffaboutmc.client.manager.ModuleManager;
import com.stuffaboutmc.client.manager.KeybindManager;
import com.stuffaboutmc.client.module.Module;

public class Client {
    public static Client instance;
    public ModuleManager moduleManager;
    public KeybindManager keybindManager;

    public Client() {
        instance = this;
        moduleManager = new ModuleManager();
        keybindManager = new KeybindManager();
    }

    public void onTick() {
        keybindManager.onTick();
        for (Module m : moduleManager.getModules()) {
            m.onTick();
        }
    }

    public void onRender() {
        for (Module m : moduleManager.getModules()) {
            m.onRender();
        }
    }
}
