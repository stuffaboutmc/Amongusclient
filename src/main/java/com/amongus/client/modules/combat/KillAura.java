package com.amongus.client.modules.combat; 
import com.amongus.client.input.InputSimulator; 
import com.amongus.client.modules.Module; 
import com.amongus.client.modules.Category; 
import net.minecraft.entity.Entity; 
import net.minecraft.entity.player.EntityPlayer; 
import net.minecraft.util.MathHelper; 
import net.minecraft.util.Vec3; 
import java.util.Comparator; 
import java.util.List; 
import java.util.stream.Collectors; 
 
public class KillAura extends Module { 
    // ---- Rotation modes ---- 
    public enum RotationMode { NONE, SMOOTH, INSTANT } 
    public enum Priority { DISTANCE, HEALTH, ANGLE } 
    public enum Sort { FOV, DISTANCE } 
 
    private RotationMode rotationMode = RotationMode.SMOOTH; 
    private Priority priority = Priority.ANGLE; 
    private Sort sort = Sort.FOV; 
 
    private float range = 4.5f; 
    private float fov = 180.0f; 
    private int cps = 12; 
    private float jitter = 0.5f; 
    private boolean throughWalls = false; 
    private boolean teams = false; 
    private boolean players = true; 
    private boolean animals = false; 
    private boolean invisibles = false; 
    private boolean autoCrit = true; 
    private boolean autoBlock = false; 
    private boolean attackWhileMoving = true; 
    private boolean randomize = true; 
    private int hitChance = 100; 
    private float smoothing = 0.5f; 
    private int attackDelay = 0; 
 
    private long lastAttack = 0; 
    private Entity target = null; 
    private float targetYaw = 0, targetPitch = 0; 
 
    public KillAura() { super("KillAura", Category.COMBAT); } 
 
    @Override 
    public void onUpdate() { 
 
        List<Entity> entities = mc.theWorld.loadedEntityList.stream() 
            .filter(e -> e != mc.thePlayer) 
            .filter(e -> { 
                if (e instanceof EntityPlayer) { 
                    EntityPlayer p = (EntityPlayer) e; 
                    if (!players) return false; 
                } else { 
                    if (!animals) return false; 
                } 
                if (mc.thePlayer.getDistanceToEntity(e)  return false; 
                float angle = getAngleToEntity(e); 
                if (angle  return false; 
                return true; 
            }) 
            .sorted(getComparator()) 
            .collect(Collectors.toList()); 
 
        if (!entities.isEmpty()) { 
            target = entities.get(0); 
            Vec3 vec = target.getPositionVector().addVector(0, target.height/2, 0); 
            double dx = vec.xCoord - mc.thePlayer.posX; 
            double dy = vec.yCoord - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()); 
            double dz = vec.zCoord - mc.thePlayer.posZ; 
            double dist = Math.sqrt(dx*dx + dz*dz); 
            targetYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90; 
            targetPitch = (float) -Math.toDegrees(Math.atan2(dy, dist)); 
 
            if (rotationMode == RotationMode.SMOOTH) { 
                smoothRotate(targetYaw, targetPitch, smoothing); 
            } else if (rotationMode == RotationMode.INSTANT) { 
                instantRotate(targetYaw, targetPitch); 
            } 
 
            long now = System.currentTimeMillis(); 
            long delay = (long)(1000.0 / cps) + attackDelay; 
            if (randomize) delay += (long)(Math.random() * 20 - 10); 
            if (now - lastAttack  { 
                        InputSimulator.holdKey(java.awt.event.KeyEvent.VK_SPACE, 50); 
                    } 
                    InputSimulator.clickLeft(); 
                    if (autoBlock) { 
                        InputSimulator.holdKey(java.awt.event.KeyEvent.VK_CONTROL, 30); 
                    } 
                    lastAttack = now; 
                } 
            } 
        } else { 
            target = null; 
        } 
    } 
 
    private Comparator<Entity> getComparator() { 
        if (sort == Sort.DISTANCE) { 
            return Comparator.comparingDouble(e -> mc.thePlayer.getDistanceToEntity(e)); 
        } else { 
            return Comparator.comparingDouble(e -> getAngleToEntity(e)); 
        } 
    } 
 
    private float getAngleToEntity(Entity e) { 
        Vec3 vec = e.getPositionVector().addVector(0, e.height/2, 0); 
        double dx = vec.xCoord - mc.thePlayer.posX; 
        double dz = vec.zCoord - mc.thePlayer.posZ; 
        double dist = Math.sqrt(dx*dx + dz*dz); 
        float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90; 
        float diff = MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw); 
        return Math.abs(diff); 
    } 
 
    private void smoothRotate(float targetYaw, float targetPitch, float speed) { 
        float currentYaw = mc.thePlayer.rotationYaw; 
        float currentPitch = mc.thePlayer.rotationPitch; 
        float diffYaw = MathHelper.wrapAngleTo180_float(targetYaw - currentYaw); 
        float diffPitch = targetPitch - currentPitch; 
        float stepYaw = diffYaw * speed * 0.1f; 
        float stepPitch = diffPitch * speed * 0.1f; 
        mc.thePlayer.rotationYaw += stepYaw; 
        mc.thePlayer.rotationPitch += stepPitch; 
    } 
 
    private void instantRotate(float targetYaw, float targetPitch) { 
        mc.thePlayer.rotationYaw = targetYaw; 
        mc.thePlayer.rotationPitch = targetPitch; 
    } 
 
    @Override 
    public void onRender3D(float pt) {} 
} 
