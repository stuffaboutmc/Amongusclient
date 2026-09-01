package com.user.utilitymod.module.modules.render;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

/**
 * Shows WASD + space/shift key states on screen. Drawing handled in event.HudRenderer.
 */
public class Keystrokes extends Module {

    public Keystrokes() {
        super("Keystrokes", Category.RENDER);
    }
}
