package myau.client.modules.combat;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class KillAura extends Module {

    public KillAura() {
        super("KillAura", "Attacks nearby players automatically", Category.COMBAT, Keyboard.KEY_NONE);
        addSetting(new Setting("Range", SettingType.NUMBER, 4.0, 1.0, 8.0));
        addSetting(new Setting("HitDelay", SettingType.BOOLEAN, true));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        Setting rangeSetting = getSetting("Range");
        double range = rangeSetting != null ? rangeSetting.getDouble() : 4.0;
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityPlayer && e != mc.thePlayer) {
                if (mc.thePlayer.getDistanceToEntity(e) < range) {
                    mc.playerController.attackEntity(mc.thePlayer, e);
                }
            }
        }
    }
}
