package myau.client.core;

import myau.client.modules.combat.AimAssist;
import myau.client.modules.combat.AutoSword;
import myau.client.modules.combat.KillAura;
import myau.client.modules.combat.Reach;
import myau.client.modules.combat.Velocity;
import myau.client.modules.movement.Fly;
import myau.client.modules.movement.NoSlow;
import myau.client.modules.movement.Scaffold;
import myau.client.modules.movement.Speed;
import myau.client.modules.movement.Sprint;
import myau.client.modules.movement.Step;
import myau.client.modules.movement.Strafe;
import myau.client.modules.player.AutoArmor;
import myau.client.modules.player.ChestStealer;
import myau.client.modules.player.FastBreak;
import myau.client.modules.player.FastPlace;
import myau.client.modules.player.NoFall;
import myau.client.modules.render.BlockESP;
import myau.client.modules.render.Chasm;
import myau.client.modules.render.ESP;
import myau.client.modules.render.Fullbright;
import myau.client.modules.render.NameTags;
import myau.client.modules.render.Tracers;
import myau.client.modules.exploit.Disabler;
import myau.client.modules.exploit.Timer;
import myau.client.modules.hud.ArrayListMod;
import myau.client.modules.hud.Coordinates;
import myau.client.modules.hud.FPS;
import myau.client.modules.hud.Keystrokes;
import myau.client.modules.hud.Notifications;
import myau.client.modules.hud.PotionEffects;
import myau.client.modules.hud.TargetHUD;
import myau.client.modules.hud.Watermark;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        modules.clear();

        // Combat
        modules.add(new AimAssist());
        modules.add(new AutoSword());
        modules.add(new KillAura());
        modules.add(new Reach());
        modules.add(new Velocity());

        // Movement
        modules.add(new Fly());
        modules.add(new NoSlow());
        modules.add(new Scaffold());
        modules.add(new Speed());
        modules.add(new Sprint());
        modules.add(new Step());
        modules.add(new Strafe());

        // Player
        modules.add(new AutoArmor());
        modules.add(new ChestStealer());
        modules.add(new FastBreak());
        modules.add(new FastPlace());
        modules.add(new NoFall());

        // Render
        modules.add(new BlockESP());
        modules.add(new Chasm());
        modules.add(new ESP());
        modules.add(new Fullbright());
        modules.add(new NameTags());
        modules.add(new Tracers());

        // Exploit
        modules.add(new Disabler());
        modules.add(new Timer());

        // HUD (MISC category)
        modules.add(new ArrayListMod());
        modules.add(new Coordinates());
        modules.add(new FPS());
        modules.add(new Keystrokes());
        modules.add(new Notifications());
        modules.add(new PotionEffects());
        modules.add(new TargetHUD());
        modules.add(new Watermark());
    }

    public static List<Module> getModules() {
        return new ArrayList<>(modules);
    }

    public static List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }

    public static Module getModule(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
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
