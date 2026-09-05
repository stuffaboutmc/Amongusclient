package com.stuffaboutmc.client.module.impl;

import com.stuffaboutmc.client.module.Module;
import net.minecraft.client.Minecraft;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", "Movement");
    }

    @Override
    public void onTick() {
        if (Minecraft.getMinecraft().thePlayer.moveForward > 0) {
            Minecraft.getMinecraft().thePlayer.setSprinting(true);
        }
    }
}
