package com.user.utilitymod.module.modules.movement;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import net.minecraft.client.settings.KeyBinding;

public class AutoWalk extends Module {

    public AutoWalk() {
        super("AutoWalk", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
    }

    @Override
    protected void onDisable() {
        if (mc.gameSettings != null) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        }
    }
}
