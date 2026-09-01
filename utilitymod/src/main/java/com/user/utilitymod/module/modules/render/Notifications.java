package com.user.utilitymod.module.modules.render;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

/**
 * When enabled, other modules being toggled on/off will pop up a small
 * fade-in/out notification in the corner of the screen. See event.HudRenderer.
 */
public class Notifications extends Module {

    public Notifications() {
        super("Notifications", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        // On by default conceptually; user can flip it off if they don't want popups.
    }
}
