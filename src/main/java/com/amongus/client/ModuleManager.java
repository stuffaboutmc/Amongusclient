package com.amongus.client;

import com.amongus.client.modules.Module;
import com.amongus.client.modules.combat.*;
import com.amongus.client.modules.render.*;
import com.amongus.client.modules.movement.*;
import com.amongus.client.modules.player.*;
import com.amongus.client.modules.misc.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new KillAura());
        modules.add(new Backtrack());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new Reach());
        modules.add(new AutoClicker());
        modules.add(new TriggerBot());
        modules.add(new BowAimBot());
        modules.add(new NoHitDelay());
        modules.add(new AutoSoup());
        modules.add(new AutoPot());
        modules.add(new ComboOneHit());
        modules.add(new AimAssist());
        modules.add(new AutoBlock());
        modules.add(new BlockHit());
        modules.add(new CriticalsPlus());
        modules.add(new FastBow());
        modules.add(new NoKnockback());
        modules.add(new ReachPlus());
        modules.add(new RodAimbot());
        modules.add(new Strafe());
        modules.add(new TargetHUD());
        modules.add(new TeleportHit());
        modules.add(new AutoWeapon());
        modules.add(new SilentAim());
        modules.add(new Hitboxes());
        modules.add(new NoSwingDelay());
        modules.add(new AutoShield());
        modules.add(new ComboMode());
        modules.add(new ClickGUI());
        modules.add(new ESP());
        modules.add(new Tracers());
        modules.add(new Nametags());
        modules.add(new Chams());
        modules.add(new XRay());
        modules.add(new FullBright());
        modules.add(new NoHurtCam());
        modules.add(new HUD());
        modules.add(new CustomFOV());
        modules.add(new Freecam());
        modules.add(new Projectiles());
        modules.add(new Breadcrumbs());
        modules.add(new BlockOverlay());
        modules.add(new Ambience());
        modules.add(new ChestESP());
        modules.add(new ItemESP());
        modules.add(new PlayerESP());
        modules.add(new MobESP());
        modules.add(new StorageESP());
        modules.add(new Waypoints());
        modules.add(new Radar());
        modules.add(new Minimap());
        modules.add(new Compass());
        modules.add(new Clock());
        modules.add(new Coordinates());
        modules.add(new FPSDisplay());
        modules.add(new PingDisplay());
        modules.add(new PotionHUD());
        modules.add(new ArmorHUD());
        modules.add(new Scaffold());
        modules.add(new Sprint());
        modules.add(new AutoJump());
        modules.add(new Jump());
        modules.add(new Sneak());
        modules.add(new Fly());
        modules.add(new Speed());
        modules.add(new Step());
        modules.add(new NoSlowdown());
        modules.add(new SafeWalk());
        modules.add(new Jesus());
        modules.add(new HighJump());
        modules.add(new LongJump());
        modules.add(new Spider());
        modules.add(new FastLadder());
        modules.add(new Phase());
        modules.add(new NoFall());
        modules.add(new Blink());
        modules.add(new InvMove());
        modules.add(new SprintStrafe());
        modules.add(new AirJump());
        modules.add(new AntiVoid());
        modules.add(new AutoWalk());
        modules.add(new BunnyHop());
        modules.add(new Glide());
        modules.add(new NoClip());
        modules.add(new Parkour());
        modules.add(new SpeedMine());
        modules.add(new StrafeBoost());
        modules.add(new WallClimb());
        modules.add(new AutoArmor());
        modules.add(new ChestStealer());
        modules.add(new FastPlace());
        modules.add(new FastBreak());
        modules.add(new AutoTool());
        modules.add(new AutoRespawn());
        modules.add(new AntiAFK());
        modules.add(new AutoEat());
        modules.add(new FastUse());
        modules.add(new Regen());
        modules.add(new NoVoid());
        modules.add(new AutoDisconnect());
        modules.add(new FakeLag());
        modules.add(new AutoHeal());
        modules.add(new InventoryCleaner());
        modules.add(new AutoSprint());
        modules.add(new AutoSneak());
        modules.add(new NoFire());
        modules.add(new NoFallPlayer());
        modules.add(new AutoFish());
        modules.add(new AutoFarm());
        modules.add(new AutoMine());
        modules.add(new AutoLoot());
        modules.add(new AutoSort());
        modules.add(new FastEatPlayer());
        modules.add(new NoPotionEffect());
        modules.add(new AutoSword());
        modules.add(new AutoBow());
        modules.add(new AutoRod());
        modules.add(new AutoPearl());
        modules.add(new AutoGG());
        modules.add(new AutoL());
        modules.add(new Spammer());
        modules.add(new AntiSpam());
        modules.add(new ChatBypass());
        modules.add(new DiscordRPC());
        modules.add(new StreamerMode());
        modules.add(new NoRotate());
        modules.add(new Derp());
        modules.add(new Headless());
        modules.add(new SpinBot());
        modules.add(new Timer());
        modules.add(new ClientSpoofer());
        modules.add(new PingSpoof());
        modules.add(new LagRange());
        modules.add(new MemoryFix());
        modules.add(new GhostHand());
        modules.add(new NoSwing());
        modules.add(new AutoAccept());
        modules.add(new ItemPhysics());
        modules.add(new FastEat());
        modules.add(new NoWeb());
        modules.add(new AntiKnockback());
        modules.add(new AutoReconnect());
        modules.add(new AutoLogin());
        modules.add(new ChatFilter());
        modules.add(new CommandSpy());
        modules.add(new DeathCoords());
        modules.add(new KillSults());
        modules.add(new NameProtect());
    }

    public void handleKey(int key) {
        for (Module m : modules) {
            if (m.getKey() == key) {
                m.toggle();
            }
        }
    }

    public List<Module> getModules() {
        return modules;
    }
}
