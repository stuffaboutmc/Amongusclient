package myau.client.module.impl;

import myau.client.module.Module;
import myau.client.settings.Setting;

public class Speed extends Module {

    public Speed() {
        super("Speed", "Movement");
    }

    @Override
    public void setupSettings() {
        addSetting(new Setting("Mode", "BHop", "Strafe", "NCP"));
        addSetting(new Setting("Speed", 1.2, 0.5, 3.0, 0.1));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer.onGround) {
            // speed logic here
        }
    }
}
