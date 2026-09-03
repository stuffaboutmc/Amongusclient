package com.amongus.client.gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiHook {
    public GuiHook() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui != null && event.gui.getClass() == GuiMainMenu.class) {
            try {
                event.gui = new AugustusMainMenu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
