package com.user.utilitymod;

import com.user.utilitymod.event.EventHandler;
import com.user.utilitymod.event.HudRenderer;
import com.user.utilitymod.module.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = UtilityMod.MODID, name = UtilityMod.NAME, version = UtilityMod.VERSION, clientSideOnly = true)
public class UtilityMod {

    public static final String MODID = "utilitymod";
    public static final String NAME = "Amongus";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static UtilityMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModuleManager.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(new HudRenderer());
    }
}
