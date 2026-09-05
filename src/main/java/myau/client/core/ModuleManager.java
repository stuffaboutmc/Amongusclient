package myau.client.core;

import myau.client.module.Module;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static List<Module> modules = new ArrayList<>();
    public static void init() {}
    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) { return null; }
    public static void onTick() {}
    public static void onUpdate() {}
    public static void onRender2D(float partialTicks) {}
}
