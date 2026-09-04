package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import com.amongus.client.utils.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public class Scaffold extends Module {
    private int blocksSinceJump = 0;
    private long lastPlaceTime = 0;
    private int prevSlot = -1;
    private Random random = new Random();

    public static boolean shouldSuppressSprint = false; // used by Sprint module

    public Scaffold() {
        super("Scaffold", Keyboard.KEY_NONE, Category.MOVEMENT, "Places blocks under you with advanced controls.");
        addSetting(new Setting("Mode", new String[]{"None","Telly","Normal","Godbridge","Snap"}, "Normal"));
        addSetting(new Setting("Rotation", new String[]{"Humanized","Instant","Normal","Custom"}, "Humanized"));
        addSetting(new Setting("CustomSpeed", 0.01, 1.0, 0.3, 0.01));
        addSetting(new Setting("CustomAcceleration", 0.0, 1.0, 0.4, 0.01));
        addSetting(new Setting("CustomNoiseYaw", 0.0, 2.0, 0.4, 0.05));
        addSetting(new Setting("CustomNoisePitch", 0.0, 1.0, 0.2, 0.05));
        addSetting(new Setting("Silent", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("MoveFix", new String[]{"None","Legit","Strict"}, "Legit"));
        addSetting(new Setting("Tower", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("PlaceDelay", 0, 500, 0, 10));
        addSetting(new Setting("AutoJump", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("KeepY", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("AutoSwitch", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("SafeWalk", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("Raycast", new String[]{"None","Basic","Legit","Advanced","Instant"}, "Basic"));
        addSetting(new Setting("TellyForward", 1, 5, 2, 1));
        addSetting(new Setting("TellyBackward", 1, 5, 2, 1));
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) {
            shouldSuppressSprint = false;
            return;
        }

        // Rotation mode and custom params
        RotationUtils.rotationMode = getSetting("Rotation").getValue();
        if (RotationUtils.rotationMode.equals("Custom")) {
            RotationUtils.customBaseSpeed = (float) getSetting("CustomSpeed").getDoubleValue();
            RotationUtils.customAcceleration = (float) getSetting("CustomAcceleration").getDoubleValue();
            RotationUtils.customNoiseYaw = (float) getSetting("CustomNoiseYaw").getDoubleValue();
            RotationUtils.customNoisePitch = (float) getSetting("CustomNoisePitch").getDoubleValue();
        }
        RotationUtils.silentRotations = getSetting("Silent").getValue().equals("On");

        // AutoJump works (KeepY no longer interferes)
        if (getSetting("AutoJump").getValue().equals("On")) {
            if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking()) {
                mc.thePlayer.jump();
            }
        }

        // Determine block position
        BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

        if (mode.equals("Telly")) {
            boolean movingForward = mc.thePlayer.moveForward > 0;
            boolean movingBackward = mc.thePlayer.moveForward < 0;
            if (movingForward || movingBackward) {
                int distance = movingForward ? (int) getSetting("TellyForward").getDoubleValue() :
                                              (int) getSetting("TellyBackward").getDoubleValue();
                Vec3 look = mc.thePlayer.getLookVec();
                double dx = look.xCoord * distance;
                double dz = look.zCoord * distance;
                if (!movingForward) {
                    dx = -dx;
                    dz = -dz;
                }
                below = new BlockPos(mc.thePlayer.posX + dx, mc.thePlayer.posY - 1, mc.thePlayer.posZ + dz);
            } else {
                below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
            }
        }

        // SafeWalk
        if (getSetting("SafeWalk").getValue().equals("On")) {
            if (mc.thePlayer.onGround && mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
                mc.thePlayer.motionX *= 0.5;
                mc.thePlayer.motionZ *= 0.5;
            }
        }

        boolean needsPlacement = mc.theWorld.getBlockState(below).getBlock() == Blocks.air;

        if (needsPlacement) {
            if (isPlacementReachable(below, getSetting("Raycast").getValue())) {
                if (mode.equals("Godbridge")) {
                    blocksSinceJump++;
                    if (blocksSinceJump >= 9) {
                        mc.thePlayer.jump();
                        blocksSinceJump = 0;
                        return;
                    }
                }

                if (mode.equals("Tower") && getSetting("Tower").getValue().equals("On") && mode.equals("Normal")) {
                    mc.thePlayer.motionY = 0.42;
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }

                // Rotate humanized
                float[] rotations = RotationUtils.getRotations(new Vec3(below.getX() + 0.5, below.getY() + 0.5, below.getZ() + 0.5));
                RotationUtils.applyRotations(rotations[0], rotations[1]);

                long baseDelay = (long) getSetting("PlaceDelay").getDoubleValue();
                // If KeepY enabled, add extra delay to allow natural jumps and maintain Y
                if (getSetting("KeepY").getValue().equals("On")) {
                    baseDelay += 100; // extra 100ms
                }
                long jitter = (long) (random.nextGaussian() * 25);
                long actualDelay = Math.max(0, baseDelay + jitter);
                if (System.currentTimeMillis() - lastPlaceTime >= actualDelay) {
                    boolean placed = tryPlaceBlock(below);
                    if (placed) {
                        lastPlaceTime = System.currentTimeMillis();
                    }
                }
            }
        } else {
            if (getSetting("AutoSwitch").getValue().equals("On") && prevSlot != -1) {
                mc.thePlayer.inventory.currentItem = prevSlot;
                prevSlot = -1;
            }
        }

        // MoveFix
        String moveFix = getSetting("MoveFix").getValue();
        shouldSuppressSprint = false;
        if (moveFix.equals("Legit")) {
            boolean allowedToSprint = mc.thePlayer.moveForward > 0 &&
                                      !mc.thePlayer.isSneaking() &&
                                      !mc.thePlayer.isCollidedHorizontally &&
                                      !needsPlacement;
            if (allowedToSprint) {
                mc.thePlayer.setSprinting(true);
            } else {
                mc.thePlayer.setSprinting(false);
                shouldSuppressSprint = true;
            }
        } else if (moveFix.equals("Strict")) {
            mc.thePlayer.setSprinting(false);
            shouldSuppressSprint = true;
            if (needsPlacement) {
                mc.thePlayer.motionX *= 0.6;
                mc.thePlayer.motionZ *= 0.6;
            }
        }
    }

    private boolean isPlacementReachable(BlockPos pos, String raycastMode) {
        switch (raycastMode) {
            case "None":
                return true;
            case "Instant":
                return true;
            case "Basic":
                double dist = mc.thePlayer.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                return dist <= 4.5;
            case "Legit":
                dist = mc.thePlayer.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if (dist > 4.5) return false;
                Vec3 eyes = mc.thePlayer.getPositionEyes(1.0F);
                Vec3 targetVec = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                return mc.theWorld.rayTraceBlocks(eyes, targetVec, false, true, false) == null;
            case "Advanced":
                dist = mc.thePlayer.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if (dist > 4.5) return false;
                Vec3 eyePos = mc.thePlayer.getPositionEyes(1.0F);
                Vec3 blockVec = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                MovingObjectPosition mop = mc.theWorld.rayTraceBlocks(eyePos, blockVec, false, true, false);
                return mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
            default:
                return true;
        }
    }

    private boolean tryPlaceBlock(BlockPos pos) {
        ItemStack held = mc.thePlayer.getHeldItem();
        if (held == null || !(held.getItem() instanceof ItemBlock)) {
            if (getSetting("AutoSwitch").getValue().equals("On")) {
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        prevSlot = mc.thePlayer.inventory.currentItem;
                        mc.thePlayer.inventory.currentItem = i;
                        held = stack;
                        break;
                    }
                }
            }
            if (held == null || !(held.getItem() instanceof ItemBlock)) return false;
        }

        Block block = ((ItemBlock) held.getItem()).getBlock();
        if (block == Blocks.air) return false;

        mc.playerController.onPlayerRightClick(
            mc.thePlayer,
            mc.theWorld,
            held,
            pos,
            EnumFacing.UP,
            new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
        );
        mc.thePlayer.swingItem();

        if (prevSlot != -1) {
            mc.thePlayer.inventory.currentItem = prevSlot;
            prevSlot = -1;
        }
        return true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        shouldSuppressSprint = false;
        if (prevSlot != -1) {
            mc.thePlayer.inventory.currentItem = prevSlot;
            prevSlot = -1;
        }
        blocksSinceJump = 0;
    }
}
