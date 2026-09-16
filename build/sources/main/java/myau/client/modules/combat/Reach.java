package myau.client.modules.combat;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {
    private double distance = 4.5;

    public Reach() {
        super("Reach", "Increases attack reach distance", Category.COMBAT, Keyboard.KEY_NONE);
        addSetting(new Setting("Distance", SettingType.NUMBER, 4.5, 3.0, 6.0));
    }

    @Override
    public void onEnable() {
        distance = getSetting("Distance").getDouble();
    }

    @Override
    public void onUpdate() {
        distance = getSetting("Distance").getDouble();
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            if (mc.objectMouseOver.entityHit != null) {
                double dist = mc.thePlayer.getDistanceToEntity(mc.objectMouseOver.entityHit);
                if (dist > 3.0 && dist <= distance) {
                    mc.playerController.attackEntity(mc.thePlayer, mc.objectMouseOver.entityHit);
                }
            }
        }
    }
}
