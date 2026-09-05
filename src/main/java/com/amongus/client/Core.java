package com.amongus.client;

import com.amongus.client.modules.*;
import com.amongus.client.modules.scaffold.Scaffold;
import com.amongus.client.modules.combat.KillAura;
import com.amongus.client.modules.disabler.Disabler;
import com.amongus.client.modules.visuals.HUD;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "amongusclient", name = "among us client Premium", version = "1.0-Premium")
public class Core {

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register modules
        ModuleManager.register(new ClickGUIModule());
        ModuleManager.register(new Scaffold());
        ModuleManager.register(new KillAura());
        ModuleManager.register(new Disabler());
        ModuleManager.register(new HUD());

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        MinecraftForge.EVENT_BUS.register(new CommandManager());

        System.out.println("among us client Premium loaded — " + ModuleManager.modules.size() + " modules registered.");
    }
}
