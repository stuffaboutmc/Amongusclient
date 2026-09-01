package com.user.utilitymod.module.modules.render;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

/**
 * Shows the player's currently worn armor pieces and held item as icons on screen.
 * Actual drawing happens in event.HudRenderer, which checks isEnabled() each frame.
 */
public class ArmorHud extends Module {

    public ArmorHud() {
        super("ArmorHUD", Category.RENDER);
    }
}
