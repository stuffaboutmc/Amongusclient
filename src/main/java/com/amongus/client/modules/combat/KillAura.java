package com.amongus.client.modules.combat;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.RotationUtils;
import net.minecraft.entity.Entity;
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
        addSetting(new Setting("Rotation", new String[]{"Legit","Custom","Snap","Smooth","Instant"}, "Legit"));
        addSetting(new Setting("Silent", new String[]{"Off","On"}, "On"));
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

        addSetting(new Setting("AntiBot", new String[]{"Off","Advanced","Custom"}, "Advanced"));
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
        RotationUtils.silentRotations = getSetting("Silent").getValue().equals("On");
        String blockMode = getSetting("AutoBlock").getValue();
        double range = getSetting("Range").getDoubleValue();
        int blockSpeed = (int) getSetting("BlockSpeed").getDoubleValue();
        int attackSpeed = (int) getSetting("AttackSpeed").getDoubleValue();

        target = findTarget(range);

        if (target == null) {
            if (!blockMode.equals("Vanilla")) unblock();
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

        updateTracking();
    }

    private void updateTracking() {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (!getSetting("AntiBot").getValue().equals("Custom")) return;
        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer) continue;
            if (getSetting("CheckNoMovement").getValue().equals("On")) {
                Vec3 last = lastPositions.get(player);
                Vec3 current = new Vec3(player.posX, player.posY, player.posZ);
                if (last != null && last.distanceTo(current) < 0.01) {
                    noMovementTicks.put(player, noMovementTicks.getOrDefault(player, 0) + 1);
                } else {
                    noMovementTicks.put(player, 0);
                }
                lastPositions.put(player, current);
            }
            if (getSetting("CheckSwing").getValue().equals("On")) {
                swingStates.put(player, player.isSwingInProgress);
            }
            if (getSetting("CheckAirTicks").getValue().equals("On")) {
                if (!player.onGround) {
                    airTicks.put(player, airTicks.getOrDefault(player, 0) + 1);
                } else {
                    airTicks.put(player, 0);
                }
            }
        }
    }

    private Backtrack getBacktrackModule() {
        if (AmongusClient.moduleManager == null) return null;
        for (Module m : AmongusClient.moduleManager.getModules()) {
            if (m instanceof Backtrack && m.isEnabled()) {
                return (Backtrack) m;
            }
        }
        return null;
    }

    private EntityLivingBase findTarget(double range) {
        EntityLivingBase best = null;
        double bestValue = Double.MAX_VALUE;
        String prioritize = getSetting("Prioritize").getValue();
        double fov = getSetting("FOV").getDoubleValue();
        int maxTargets = (int) getSetting("MaxTargets").getDoubleValue();
        int count = 0;

        for (Object obj : mc.theWorld.loadedEntityList) {
            if (!(obj instanceof EntityLivingBase)) continue;
            EntityLivingBase entity = (EntityLivingBase) obj;
            if (entity == mc.thePlayer || entity.isDead || entity.getHealth() <= 0) continue;

            boolean matchesType = false;
            if (getSetting("TargetPlayers").getValue().equals("On") && entity instanceof EntityPlayer) matchesType = true;
            if (getSetting("TargetMobs").getValue().equals("On") && entity instanceof EntityMob) matchesType = true;
            if (getSetting("TargetAnimals").getValue().equals("On") && entity instanceof EntityAnimal) matchesType = true;
            if (getSetting("TargetInvisible").getValue().equals("On") && entity.isInvisible()) matchesType = true;
            if (!matchesType) continue;

            if (entity.isInvisible() && getSetting("Invisibles").getValue().equals("Off") &&
                getSetting("TargetInvisible").getValue().equals("Off")) continue;

            if (isBot(entity)) continue;

            if (getSetting("ThroughWalls").getValue().equals("Off") && !mc.thePlayer.canEntityBeSeen(entity)) continue;
            if (!isInFOV(entity, fov)) continue;

            double dist = mc.thePlayer.getDistanceToEntity(entity);
            if (dist > range) continue;

            if (getSetting("IgnoreTeammates").getValue().equals("On") && entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (ignoredTeammates.contains(player)) continue;
                if (!confirmedEnemies.contains(player)) {
                    if (player.hurtTime > 0) {
                        confirmedEnemies.add(player);
                    } else {
                        ignoredTeammates.add(player);
                        continue;
                    }
                }
            }

            double value = dist;
            if (prioritize.equals("LowestHP")) value = entity.getHealth();
            else if (prioritize.equals("HighestHP")) value = -entity.getHealth();

            if (value < bestValue) {
                bestValue = value;
                best = entity;
                count++;
                if (count >= maxTargets) break;
            }
        }
        return best;
    }

    private boolean isBot(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) return false;
        EntityPlayer player = (EntityPlayer) entity;
        String antiBotMode = getSetting("AntiBot").getValue();

        if (antiBotMode.equals("Off")) return false;

        if (antiBotMode.equals("Advanced")) {
            if (mc.getNetHandler() != null &&
                mc.getNetHandler().getPlayerInfo(player.getUniqueID()) == null) return true;
            return player.getName().contains("§");
        }

        if (antiBotMode.equals("Custom")) {
            if (getSetting("CheckTab").getValue().equals("On")) {
                if (mc.getNetHandler() != null &&
                    mc.getNetHandler().getPlayerInfo(player.getUniqueID()) == null) return true;
            }
            if (getSetting("CheckName").getValue().equals("On")) {
                if (player.getName().contains("§")) return true;
            }
            if (getSetting("CheckPing").getValue().equals("On")) {
                if (mc.getNetHandler() != null &&
                    mc.getNetHandler().getPlayerInfo(player.getUniqueID()) != null &&
                    mc.getNetHandler().getPlayerInfo(player.getUniqueID()).getResponseTime() > 1000) return true;
            }
            if (getSetting("CheckUUID").getValue().equals("On")) {
                if (player.getUniqueID().version() == 0) return true;
            }
            if (getSetting("CheckHealth").getValue().equals("On")) {
                if (player.getHealth() > 40 || player.getMaxHealth() > 40) return true;
            }
            if (getSetting("CheckDuplicateName").getValue().equals("On")) {
                if (hasDuplicateName(player)) return true;
            }
            if (getSetting("CheckNoMovement").getValue().equals("On")) {
                if (noMovementTicks.getOrDefault(player, 0) > 100) return true;
            }
            if (getSetting("CheckPitch").getValue().equals("On")) {
                if (player.rotationPitch > 90 || player.rotationPitch < -90) return true;
            }
            if (getSetting("CheckNameLength").getValue().equals("On")) {
                if (player.getName().length() > 16 || player.getName().length() < 3) return true;
            }
            if (getSetting("CheckNumericName").getValue().equals("On")) {
                if (player.getName().matches("\\d+")) return true;
            }
            if (getSetting("CheckSwing").getValue().equals("On")) {
                if (!swingStates.getOrDefault(player, true)) return true;
            }
            if (getSetting("CheckInvisible").getValue().equals("On")) {
                if (player.isInvisible()) return true;
            }
            if (getSetting("CheckSneaking").getValue().equals("On")) {
                if (!player.isSneaking()) return true;
            }
            if (getSetting("CheckSprinting").getValue().equals("On")) {
                if (!player.isSprinting()) return true;
            }
            if (getSetting("CheckArmor").getValue().equals("On")) {
                boolean hasArmor = false;
                for (ItemStack armor : player.inventory.armorInventory) {
                    if (armor != null) { hasArmor = true; break; }
                }
                if (!hasArmor) return true;
            }
            if (getSetting("CheckHeldItem").getValue().equals("On")) {
                if (player.getHeldItem() == null) return true;
            }
            if (getSetting("CheckYaw").getValue().equals("On")) {
                if (Float.isNaN(player.rotationYaw) || Float.isInfinite(player.rotationYaw)) return true;
            }
            if (getSetting("CheckDistance").getValue().equals("On")) {
                if (mc.thePlayer.getDistanceToEntity(player) > 100) return true;
            }
            if (getSetting("CheckAirTicks").getValue().equals("On")) {
                if (airTicks.getOrDefault(player, 0) > 60) return true;
            }
            return false;
        }
        return false;
    }

    private boolean hasDuplicateName(EntityPlayer player) {
        for (EntityPlayer p : mc.theWorld.playerEntities) {
            if (p != player && p.getName().equals(player.getName())) return true;
        }
        return false;
    }

    private boolean isInFOV(EntityLivingBase entity, double fov) {
        double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        double pitch = Math.toRadians(mc.thePlayer.rotationPitch);
        Vec3 look = new Vec3(
            -Math.sin(yaw) * Math.cos(pitch),
            -Math.sin(pitch),
            Math.cos(yaw) * Math.cos(pitch)
        ).normalize();
        Vec3 toEntity = entity.getPositionVector()
            .subtract(mc.thePlayer.getPositionEyes(1.0F)).normalize();
        double angle = Math.toDegrees(Math.acos(look.dotProduct(toEntity)));
        return angle <= fov / 2.0;
    }

    private void attack(EntityLivingBase target) {
        if (getSetting("SwordOnly").getValue().equals("On") &&
            (mc.thePlayer.getHeldItem() == null ||
             !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))) return;
        if (getSetting("Criticals").getValue().equals("On")) mc.thePlayer.onGround = false;
        mc.thePlayer.swingItem();
        mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }

    private void startBlock() {
        if (!blocking && mc.thePlayer.getHeldItem() != null &&
            mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            blocking = true;
        }
    }

    private void sendBlockPacket() {
        if (mc.thePlayer.getHeldItem() != null &&
            mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
        }
    }

    private void unblock() {
        if (blocking) {
            mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
                C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blocking = false;
        }
    }

    @Override
    public void onDisable() {
        unblock();
        target = null;
        confirmedEnemies.clear();
        ignoredTeammates.clear();
        lastPositions.clear();
        noMovementTicks.clear();
        airTicks.clear();
        swingStates.clear();
    }
}
