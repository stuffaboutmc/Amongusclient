package com.user.utilitymod.module.modules.movement;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.Setting;

public class Speed extends Module {

    public Speed() {
        super("Speed", Category.MOVEMENT);
        addSetting(Setting.slider("Multiplier", 1.3, 1.0, 3.0, 0.05));
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;
        if (!mc.thePlayer.onGround) return;

        double multiplier = getSetting("Multiplier").getValue();
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);

        double motionX = mc.thePlayer.motionX;
        double motionZ = mc.thePlayer.motionZ;
        double speed = Math.sqrt(motionX * motionX + motionZ * motionZ);

        if (speed < 0.001) return;

        mc.thePlayer.motionX = motionX * multiplier;
        mc.thePlayer.motionZ = motionZ * multiplier;
    }
}
