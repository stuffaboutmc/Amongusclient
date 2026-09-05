package com.stuffaboutmc.client.manager;

import com.stuffaboutmc.client.module.Module;
import com.stuffaboutmc.client.module.impl.Flight;
import com.stuffaboutmc.client.module.impl.Speed;
import com.stuffaboutmc.client.module.impl.KillAura;
import com.stuffaboutmc.client.module.impl.Sprint;
import com.stuffaboutmc.client.module.impl.FullBright;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new Flight());
        modules.add(new Speed());
        modules.add(new KillAura());
        modules.add(new Sprint());
        modules.add(new FullBright());
    }

    public List<Module> getModules() { return modules; }
    public Module getModuleByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}
