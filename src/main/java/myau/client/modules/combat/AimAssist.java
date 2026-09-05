package myau.client.modules.combat;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class AimAssist extends Module {
    private int speed = 5;
    private double range = 4.5;
    private int tickCounter = 0;

    public AimAssist() {
        super("AimAssist", "Slowly rotates towards nearest player", Category.COMBAT, Keyboard.KEY_NONE);
        addSetting(new Setting("Speed", SettingType.NUMBER, 5, 1, 10));
        addSetting(new Setting("Range", SettingType.NUMBER, 4.5, 2.0, 6.0));
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        tickCounter++;
        if (tickCounter % 2 != 0) return;

        Entity target = findTarget();
        if (target == null) return;

        float[] targetRot = getRotationsTo(target);
        float yawDiff = targetRot[0] - mc.thePlayer.rotationYaw;
        float pitchDiff = targetRot[1] - mc.thePlayer.rotationPitch;

        while (yawDiff > 180) yawDiff -= 360;
        while (yawDiff < -180) yawDiff += 360;

        float factor = speed / 10.0F;
        mc.thePlayer.rotationYaw += yawDiff * factor;
        mc.thePlayer.rotationPitch += pitchDiff * factor;
    }

    private Entity findTarget() {
        Entity closest = null;
        double closestDist = range;
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityPlayer && obj != mc.thePlayer) {
                EntityPlayer p = (EntityPlayer) obj;
                if (p.isDead || p.getHealth() <= 0) continue;
                double dist = mc.thePlayer.getDistanceToEntity(p);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = p;
                }
            }
        }
        return closest;
    }

    private float[] getRotationsTo(Entity entity) {
        double diffX = entity.posX - mc.thePlayer.posX;
        double diffY = (entity.posY + entity.getEyeHeight()) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double diffZ = entity.posZ - mc.thePlayer.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, dist));
        return new float[]{yaw, pitch};
    }
}
