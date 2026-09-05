package com.amongus.client;

import com.amongus.client.modules.*;
import com.amongus.client.modules.scaffold.Scaffold;
import com.amongus.client.modules.combat.KillAura;
import com.amongus.client.modules.disabler.Disabler;
import com.amongus.client.modules.visuals.HUD;

public class Core {
    public static void init() {
        // Register all modules
        ModuleManager.register(new ClickGUIModule());  // opens GUI, stores style
        ModuleManager.register(new Scaffold());
        ModuleManager.register(new KillAura());
        ModuleManager.register(new Disabler());
        ModuleManager.register(new HUD());

        System.out.println("among us client Premium loaded — " + ModuleManager.modules.size() + " modules registered.");
    }
}
