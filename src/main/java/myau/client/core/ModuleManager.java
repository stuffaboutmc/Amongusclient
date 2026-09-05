package myau.client.core;

import myau.client.module.Module;
import myau.client.module.impl.Flight;
import myau.client.module.impl.Speed;
import myau.client.module.impl.Sprint;
import myau.client.module.impl.FullBright;
import myau.client.module.impl.KillAura;
import myau.client.module.impl.Scaffold; // add if exists

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.add(new Flight());
        modules.add(new Speed());
        modules.add(new Sprint());
        modules.add(new FullBright());
        modules.add(new KillAura());
        modules.add(new Scaffold());
    }

    public static List<Module> getModules() { return modules; }

    public static Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    public static void onTick() {
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick();
        }
    }

    public static void onUpdate() {
        for (Module m : modules) {
            if (m.isEnabled()) m.onUpdate();
        }
    }

    public static void onRender2D(float partialTicks) {
        for (Module m : modules) {
            if (m.isEnabled()) m.onRender2D(partialTicks);
        }
    }
}
