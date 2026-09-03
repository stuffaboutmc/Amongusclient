package com.amongus.client;

import com.amongus.client.gui.GuiHook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "amongus", name = "Amongus Client", version = "1.0")
public class AmongusClient {
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        CommandManager.setModules(moduleManager.getModules());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(moduleManager);
        MinecraftForge.EVENT_BUS.register(commandManager);
        new GuiHook();
        System.out.println("[Amongus] Client initialized.");
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) moduleManager.handleKey(Keyboard.getEventKey());
    }
}
