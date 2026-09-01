package com.user.utilitymod.module.modules.movement;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

public class Sprint extends Module {

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;
        if (mc.thePlayer.getFoodStats().getFoodLevel() > 6 || mc.thePlayer.capabilities.isFlying) {
            mc.thePlayer.setSprinting(true);
        }
    }

    @Override
    protected void onDisable() {
        if (mc.thePlayer != null) mc.thePlayer.setSprinting(false);
    }
}
