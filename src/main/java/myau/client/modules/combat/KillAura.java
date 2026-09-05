package myau.client.modules.combat;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class KillAura extends Module {
    private double range = 6.0;
    private int delay = 0;
    private int tickDelay = 0;

    public KillAura() {
        super("KillAura", "Attacks nearest player in range", Category.COMBAT, Keyboard.KEY_R);
        addSetting(new Setting("Range", SettingType.NUMBER, 6.0, 3.0, 6.0));
    }

    @Override
    public void onEnable() {
        tickDelay = 0;
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        tickDelay++;
        if (tickDelay < 4) return;
        tickDelay = 0;

        Entity target = findTarget();
        if (target == null) return;

        float[] rotations = getRotationsTo(target);
        mc.thePlayer.rotationYaw = rotations[0];
        mc.thePlayer.rotationPitch = rotations[1];

        if (mc.thePlayer.getDistanceToEntity(target) <= range) {
            mc.playerController.attackEntity(mc.thePlayer, target);
            mc.thePlayer.swingItem();
        }
    }

    private Entity findTarget() {
        Entity closest = null;
        double closestDist = range;
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityPlayer && obj != mc.thePlayer) {
                EntityPlayer player = (EntityPlayer) obj;
                if (player.isDead || player.getHealth() <= 0) continue;
                double dist = mc.thePlayer.getDistanceToEntity(player);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = player;
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
