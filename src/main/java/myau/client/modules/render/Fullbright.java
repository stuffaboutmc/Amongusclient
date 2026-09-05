package com.stuffaboutmc.client.module.impl;

import com.stuffaboutmc.client.module.Module;
import net.minecraft.client.Minecraft;

public class FullBright extends Module {

    private float oldGamma;

    public FullBright() {
        super("FullBright", "Visual");
    }

    @Override
    public void onEnable() {
        oldGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 100f;
    }

    @Override
    public void onDisable() {
        Minecraft.getMinecraft().gameSettings.gammaSetting = oldGamma;
    }
}
 
