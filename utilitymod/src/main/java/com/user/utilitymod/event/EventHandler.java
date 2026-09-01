package com.user.utilitymod.event;

import com.user.utilitymod.gui.ClickGui;
import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class EventHandler {

    public static final int GUI_OPEN_KEY = Keyboard.KEY_RSHIFT;

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        ModuleManager.onTick();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (mc.thePlayer == null) return;

        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();

            if (key == GUI_OPEN_KEY && mc.currentScreen == null) {
                mc.displayGuiScreen(new ClickGui());
                return;
            }

            for (Module m : ModuleManager.getModules()) {
                if (m.getKeybind() != 0 && m.getKeybind() == key && mc.currentScreen == null) {
                    // Zoom-style "hold" modules read Keyboard.isKeyDown directly in onTick,
                    // so only toggle here for modules that don't manage their own hold state.
                    if (!m.getName().equals("Zoom")) {
                        m.toggle();
                    }
                }
            }
        }
    }
}
