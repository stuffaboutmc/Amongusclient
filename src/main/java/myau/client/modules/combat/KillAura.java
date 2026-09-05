package com.stuffaboutmc.client.module.impl;

import com.stuffaboutmc.client.module.Module;
import com.stuffaboutmc.client.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class KillAura extends Module {

    public KillAura() {
        super("KillAura", "Combat");
    }

    @Override
    public void setupSettings() {
        addSetting(new Setting("Range", 4.0, 1.0, 8.0, 0.1));
        addSetting(new Setting("HitDelay", true));
    }

    @Override
    public void onTick() {
        for (Entity e : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (e instanceof EntityPlayer && e != Minecraft.getMinecraft().thePlayer) {
                if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) < 4.0) {
                    Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, e);
                }
            }
        }
    }
}
