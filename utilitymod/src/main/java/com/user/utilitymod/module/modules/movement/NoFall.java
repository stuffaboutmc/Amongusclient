package com.user.utilitymod.module.modules.movement;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;

public class NoFall extends Module {

    public NoFall() {
        super("NoFall", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;
        if (mc.thePlayer.fallDistance > 2.0f && !mc.thePlayer.onGround) {
            mc.thePlayer.fallDistance = 0.0f;
        }
    }
}
