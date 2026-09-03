package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class KillAura extends Module {
    private EntityLivingBase target;
    private long lastAttackTime;
    private boolean blocking;
    private long lastBlockToggle = 0;
    public KillAura() {
        super("KillAura", Keyboard.KEY_R, Category.COMBAT, "Hits anything close by.");
        addSetting(new Setting("Rotation", new String[]{"Legit","Custom","Snap","Smooth","Instant"}, "Legit"));
        addSetting(new Setting("Silent", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("AutoBlock", new String[]{"None","Legit","Packet","Vanilla"}, "None"));
        addSetting(new Setting("BlockSpeed", 1, 20, 10, 1));
        addSetting(new Setting("AttackSpeed", 1, 20, 10, 1));
        addSetting(new Setting("Range", 3, 8, 6, 0.5));
        addSetting(new Setting("TargetMode", new String[]{"Nearest","LowestHP","HighestHP","Players","Mobs","All"}, "Nearest"));
        addSetting(new Setting("Invisibles", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("ThroughWalls", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("SwordOnly", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("Criticals", new String[]{"Off","On"}, "Off"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        RotationUtils.rotationMode = getSetting("Rotation").getValue();
        RotationUtils.silentRotations = getSetting("Silent").getValue().equals("On");
        String blockMode = getSetting("AutoBlock").getValue();
        double range = getSetting("Range").getDoubleValue();
        int blockSpeed = (int) getSetting("BlockSpeed").getDoubleValue();
        int attackSpeed = (int) getSetting("AttackSpeed").getDoubleValue();
        target = findTarget(range);
        if (target == null) { if (!blockMode.equals("Vanilla")) unblock(); return; }
        float[] rotations = RotationUtils.getRotations(target);
        RotationUtils.applyRotations(rotations[0], rotations[1]);
        long currentTime = System.currentTimeMillis();
        if (blockMode.equals("Legit")) {
            if (currentTime - lastBlockToggle >= 50 * blockSpeed) { if (!blocking) { startBlock(); lastBlockToggle = currentTime; } }
            if (!blocking && currentTime - lastAttackTime >= 50 * attackSpeed) { attack(target); lastAttackTime = currentTime; }
            if (blocking && currentTime - lastBlockToggle >= 50 * blockSpeed) { unblock(); lastBlockToggle = currentTime; }
        } else if (blockMode.equals("Packet")) {
            if (currentTime - lastBlockToggle >= 50 * blockSpeed) { sendBlockPacket(); lastBlockToggle = currentTime; }
            if (currentTime - lastAttackTime >= 50 * attackSpeed) { attack(target); lastAttackTime = currentTime; }
        } else if (blockMode.equals("Vanilla")) {
            startBlock();
            if (currentTime - lastAttackTime >= 50 * attackSpeed) { attack(target); lastAttackTime = currentTime; }
        } else {
            if (currentTime - lastAttackTime >= 50 * attackSpeed) { attack(target); lastAttackTime = currentTime; }
        }
    }
    private EntityLivingBase findTarget(double range) {
        EntityLivingBase best = null; double bestValue = Double.MAX_VALUE;
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity == mc.thePlayer || entity.isDead || entity.getHealth() <= 0) continue;
            if (getSetting("Invisibles").getValue().equals("Off") && entity.isInvisible()) continue;
            double dist = mc.thePlayer.getDistanceToEntity(entity);
            if (dist > range) continue;
            if (getSetting("ThroughWalls").getValue().equals("Off") && !mc.thePlayer.canEntityBeSeen(entity)) continue;
            if (dist < bestValue) { bestValue = dist; best = entity; }
        }
        return best;
    }
    private void attack(EntityLivingBase target) {
        if (getSetting("SwordOnly").getValue().equals("On") && (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))) return;
        if (getSetting("Criticals").getValue().equals("On")) mc.thePlayer.onGround = false;
        mc.thePlayer.swingItem();
        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }
    private void startBlock() { if (!blocking && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) { mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem())); blocking = true; } }
    private void sendBlockPacket() { if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) { mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem())); } }
    private void unblock() { if (blocking) { mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN)); blocking = false; } }
    @Override
    public void onDisable() { unblock(); target = null; }
}
