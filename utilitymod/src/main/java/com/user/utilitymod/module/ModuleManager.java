package com.user.utilitymod.module;

import com.user.utilitymod.module.modules.misc.AutoTool;
import com.user.utilitymod.module.modules.misc.FastPlace;
import com.user.utilitymod.module.modules.misc.NoRain;
import com.user.utilitymod.module.modules.misc.Timer;
import com.user.utilitymod.module.modules.movement.AutoWalk;
import com.user.utilitymod.module.modules.movement.Fly;
import com.user.utilitymod.module.modules.movement.NoFall;
import com.user.utilitymod.module.modules.movement.Speed;
import com.user.utilitymod.module.modules.movement.Sprint;
import com.user.utilitymod.module.modules.movement.Step;
import com.user.utilitymod.module.modules.render.ArmorHud;
import com.user.utilitymod.module.modules.render.Fullbright;
import com.user.utilitymod.module.modules.render.Keystrokes;
import com.user.utilitymod.module.modules.render.Notifications;
import com.user.utilitymod.module.modules.render.Zoom;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static final List<Module> MODULES = new ArrayList<>();

    public static void init() {
        // Movement
        register(new Sprint());
        register(new Speed());
        register(new Fly());
        register(new NoFall());
        register(new Step());
        register(new AutoWalk());

        // Render
        register(new Fullbright());
        register(new Zoom());
        register(new ArmorHud());
        register(new Keystrokes());
        register(new Notifications());

        // Combat — intentionally left as an empty category shell.
        // Automated-attack modules (KillAura and similar) are not included.

        // Misc
        register(new FastPlace());
        register(new AutoTool());
        register(new NoRain());
        register(new Timer());
    }

    private static void register(Module module) {
        MODULES.add(module);
    }

    public static List<Module> getModules() {
        return MODULES;
    }

    public static List<Module> getModulesByCategory(Category category) {
        List<Module> list = new ArrayList<>();
        for (Module m : MODULES) {
            if (m.getCategory() == category) list.add(m);
        }
        return list;
    }

    public static Module getModuleByName(String name) {
        for (Module m : MODULES) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    public static void onTick() {
        for (Module m : MODULES) {
            if (m.isEnabled()) m.onTick();
        }
    }
}
