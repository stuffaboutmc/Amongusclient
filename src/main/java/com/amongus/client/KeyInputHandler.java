package com.amongus.client;

import com.amongus.client.modules.ClickGUIModule;
import com.amongus.client.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyInputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(54)) { // Right Shift
            for (Module mod : ModuleManager.modules) {
                if (mod instanceof ClickGUIModule) {
                    mod.toggle();
                    break;
                }
            }
        }
    }
}
