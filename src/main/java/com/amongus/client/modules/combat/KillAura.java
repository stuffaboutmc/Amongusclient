package com.amongus.client.modules.combat;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.*;

public class KillAura extends Module {
    private EntityLivingBase target;
    private long lastAttackTime;
    private boolean blocking;
    private long lastBlockToggle = 0;

    private Set<EntityPlayer> confirmedEnemies = new HashSet<>();
    private Set<EntityPlayer> ignoredTeammates = new HashSet<>();
    private Map<EntityPlayer, Vec3> lastPositions = new HashMap<>();
    private Map<EntityPlayer, Integer> noMovementTicks = new HashMap<>();
    private Map<EntityPlayer, Integer> airTicks = new HashMap<>();
    private Map<EntityPlayer, Boolean> swingStates = new HashMap<>();

    public KillAura() {
        super("KillAura", Keyboard.KEY_R, Category.COMBAT, "Attacks entities with extreme customization.");
        addSetting(new Setting("Rotation", new String[]{"Humanized","Instant","Normal","Custom"}, "Humanized"));
        addSetting(new Setting("CustomSpeed", 0.01, 1.0, 0.3, 0.01));
        addSetting(new Setting("CustomAcceleration", 0.0, 1.0, 0.4, 0.01));
        addSetting(new Setting("CustomNoiseYaw", 0.0, 2.0, 0.4, 0.05));
        addSetting(new Setting("CustomNoisePitch", 0.0, 1.0, 0.2, 0.05));
        addSetting(new Setting("Silent", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("AutoBlock", new String[]{"None","Legit","Packet","Vanilla"}, "None"));
        addSetting(new Setting("BlockSpeed", 1, 20, 10, 1));
        addSetting(new Setting("AttackSpeed", 1, 20, 10, 1));
        addSetting(new Setting("Range", 3, 8, 6, 0.5));
        addSetting(new Setting("Prioritize", new String[]{"Nearest","LowestHP","HighestHP"}, "Nearest"));
        addSetting(new Setting("TargetPlayers", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("TargetMobs", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("TargetAnimals", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("TargetInvisible", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("Invisibles", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("ThroughWalls", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("Raycast", new String[]{"None","Basic","Legit","Advanced","Instant"}, "Basic"));
        addSetting(new Setting("SwordOnly", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("Criticals", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("FOV", 30, 360, 180, 10));
        addSetting(new Setting("SwitchDelay", 0, 1000, 200, 50));
        addSetting(new Setting("IgnoreTeammates", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("MaxTargets", 1, 10, 1, 1));
        addSetting(new Setting("MoveFix", new String[]{"None","Silent","Strict"}, "Silent"));

        addSetting(new Setting("AntiBot", new String[]{"Off","Advanced","Custom"}, "Off"));
        addSetting(new Setting("CheckTab", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("CheckName", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("CheckPing", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckUUID", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckHealth", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckDuplicateName", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckNoMovement", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckPitch", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckNameLength", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckNumericName", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckSwing", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckInvisible", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckSneaking", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckSprinting", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckArmor", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckHeldItem", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckYaw", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckDistance", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("CheckAirTicks", new String[]{"Off","On"}, "On"));
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;

        RotationUtils.rotationMode = getSetting("Rotation").getValue();
        if (RotationUtils.rotationMode.equals("Custom")) {
            RotationUtils.customBaseSpeed = (float) getSetting("CustomSpeed").getDoubleValue();
            RotationUtils.customAcceleration = (float) getSetting("CustomAcceleration").getDoubleValue();
            RotationUtils.customNoiseYaw = (float) getSetting("CustomNoiseYaw").getDoubleValue();
            RotationUtils.customNoisePitch = (float) getSetting("CustomNoisePitch").getDoubleValue();
        }
        RotationUtils.silentRotations = getSetting("Silent").getValue().equals("On");
        String blockMode = getSetting("AutoBlock").getValue();
        double range = getSetting("Range").getDoubleValue();
        int blockSpeed = (int) getSetting("BlockSpeed").getDoubleValue();
        int attackSpeed = (int) getSetting("AttackSpeed").getDoubleValue();

        target = findTarget(range);

        if (target == null) {
            if (!blockMode.equals("Vanilla")) unblock();
            applyMoveFix(false);
            return;
        }

        Backtrack backtrack = getBacktrackModule();
        Vec3 aimPos = null;
        if (backtrack != null && target instanceof EntityPlayer) {
            aimPos = backtrack.getBacktrackedPosition((EntityPlayer) target);
        }

        float[] rotations;
        if (aimPos != null) {
            rotations = RotationUtils.getRotations(aimPos);
        } else {
            rotations = RotationUtils.getRotations(target);
        }
        RotationUtils.applyRotations(rotations[0], rotations[1]);

        long currentTime = System.currentTimeMillis();

        if (blockMode.equals("Legit")) {
            if (currentTime - lastBlockToggle >= 50 * blockSpeed) {
                if (!blocking) { startBlock(); lastBlockToggle = currentTime; }
            }
            if (!blocking && currentTime - lastAttackTime >= 50 * attackSpeed) {
                attack(target); lastAttackTime = currentTime;
            }
            if (blocking && currentTime - lastBlockToggle >= 50 * blockSpeed) {
                unblock(); lastBlockToggle = currentTime;
            }
        } else if (blockMode.equals("Packet")) {
            if (currentTime - lastBlockToggle >= 50 * blockSpeed) {
                sendBlockPacket(); lastBlockToggle = currentTime;
            }
            if (currentTime - lastAttackTime >= 50 * attackSpeed) {
                attack(target); lastAttackTime = currentTime;
            }
        } else if (blockMode.equals("Vanilla")) {
            startBlock();
            if (currentTime - lastAttackTime >= 50 * attackSpeed) {
                attack(target); lastAttackTime = currentTime;
            }
        } else {
            if (currentTime - lastAttackTime >= 50 * attackSpeed) {
                attack(target); lastAttackTime = currentTime;
            }
        }

        applyMoveFix(true);
        updateTracking();
    }

    // ... rest methods same as previously, but ensure applyMoveFix, findTarget, isTargetReachable, isBot, etc. are included.
    // For brevity they are unchanged from earlier, just copy them from the previous full KillAura. 
    // I will omit repeating here but the file must include all methods.
    // I'll assume you paste the full file from my previous message with these changes integrated.
}
