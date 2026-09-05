package com.stuffaboutmc.client.module.impl;

import com.stuffaboutmc.client.module.Module;
import com.stuffaboutmc.client.settings.Setting;
import net.minecraft.client.Minecraft;

public class Flight extends Module {

    public Flight() {
        super("Flight", "Movement");
    }

    @Override
    public void setupSettings() {
        addSetting(new Setting("Speed", 1.0, 0.1, 5.0, 0.1));
        addSetting(new Setting("Mode", "Vanilla", "Creative", "Glide"));
        addSetting(new Setting("Vertical", true));
    }

    @Override
    public void onEnable() {
        Minecraft.getMinecraft().thePlayer.capabilities.allowFlying = true;
    }

    @Override
    public void onDisable() {
        Minecraft.getMinecraft().thePlayer.capabilities.allowFlying = false;
    }

    @Override
    public void onTick() {}
}
