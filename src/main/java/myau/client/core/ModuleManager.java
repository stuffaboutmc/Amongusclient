package myau.client.core;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import myau.client.modules.combat.*;
import myau.client.modules.movement.*;
import myau.client.modules.player.*;
import myau.client.modules.render.*;
import myau.client.modules.exploit.*;
import myau.client.modules.hud.*;

public class ModuleManager {
    private static final List<Module> modules = new CopyOnWriteArrayList<>();
    private static final Map<String, Module> moduleMap = new HashMap<>();

    public static void init() {
        modules.clear();
        moduleMap.clear();
        register(new KillAura());
        register(new Velocity());
        register(new AimAssist());
        register(new AutoSword());
        register(new Reach());
        register(new Sprint());
        register(new Fly());
        register(new Speed());
        register(new Scaffold());
        register(new NoSlow());
        register(new Step());
        register(new Strafe());
        register(new AutoArmor());
        register(new ChestStealer());
        register(new FastPlace());
        register(new NoFall());
        register(new FastBreak());
        register(new ESP());
        register(new Chasm());
        register(new Tracers());
        register(new Fullbright());
        register(new BlockESP());
        register(new NameTags());
        register(new Disabler());
        register(new myau.client.modules.exploit.Timer());
        register(new Watermark());
        register(new ArrayListMod());
        register(new Notifications());
        register(new TargetHUD());
        register(new Keystrokes());
        register(new Coordinates());
        register(new FPS());
        register(new PotionEffects());
    }

    private static void register(Module m) {
        modules.add(m);
        moduleMap.put(m.getName().toLowerCase(), m);
    }

    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) { return moduleMap.get(name.toLowerCase()); }

    public static List<Module> getByCategory(Category cat) {
        List<Module> list = new ArrayList<>();
        for (Module m : modules) if (m.getCategory() == cat) list.add(m);
        return list;
    }

    public static void onUpdate() { for (Module m : modules) if (m.isEnabled()) m.onUpdate(); }
    public static void onTick() { for (Module m : modules) if (m.isEnabled()) m.onTick(); }
    public static void onRender2D(float pt) { for (Module m : modules) if (m.isEnabled()) m.onRender2D(pt); }
    public static void onRender3D(float pt) { for (Module m : modules) if (m.isEnabled()) m.onRender3D(pt); }
}
