package myau.client.module.impl;

import myau.client.module.Module;
import myau.client.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class KillAura extends Module {

    public KillAura() {
        super("KillAura", "Combat");
    }

    @Override
    public void setupSettings() {
        addSetting(new Setting("Range", 4.0, 1.0, 8.0, 0.1));
        addSetting(new Setting("HitDelay", true));
    }

    @Override
    public void onUpdate() {
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityPlayer && e != mc.thePlayer) {
                if (mc.thePlayer.getDistanceToEntity(e) < 4.0) {
                    mc.playerController.attackEntity(mc.thePlayer, e);
                }
            }
        }
    }
}
