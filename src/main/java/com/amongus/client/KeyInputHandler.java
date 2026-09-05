package com.amongus.client;

import com.amongus.client.modules.ClickGUIModule;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // Check if the ClickGUI key (Right Shift, keycode 54) is pressed
        if (Keyboard.isKeyDown(54)) {
            for (Module mod : ModuleManager.modules) {
                if (mod instanceof ClickGUIModule) {
                    mod.toggle(); // opens the GUI via onEnable()
                    break;
                }
            }
        }
    }
}
