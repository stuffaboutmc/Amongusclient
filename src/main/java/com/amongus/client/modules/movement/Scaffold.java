package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
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

public class Scaffold extends Module {
    private int blocksSinceJump = 0;
    private long lastPlaceTime = 0;
    private int prevSlot = -1;
    private double keepYLevel = 0;
    private boolean keepYInitialized = false;

    public Scaffold() {
        super("Scaffold", Keyboard.KEY_NONE, Category.MOVEMENT, "Places blocks under you with advanced controls.");
        addSetting(new Setting("Mode", new String[]{"None","Telly","Normal","Godbridge","Snap"}, "Normal"));
        addSetting(new Setting("Rotate", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("MoveFix", new String[]{"None","Silent","Strict"}, "Silent"));
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
        if (mode.equals("None")) return;

        if (getSetting("KeepY").getValue().equals("On")) {
            if (!keepYInitialized) {
                keepYLevel = Math.floor(mc.thePlayer.posY);
                keepYInitialized = true;
            }
        } else {
            keepYInitialized = false;
        }

        if (getSetting("AutoJump").getValue().equals("On")) {
            if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking()) {
                if (getSetting("KeepY").getValue().equals("On")) {
                    mc.thePlayer.motionY = 0;
                } else {
                    mc.thePlayer.jump();
                }
            }
        }

        if (getSetting("KeepY").getValue().equals("On")) {
            double currentY = mc.thePlayer.posY;
            if (currentY > keepYLevel) {
                mc.thePlayer.motionY -= 0.1;
                if (mc.thePlayer.motionY < -0.5) mc.thePlayer.motionY = -0.5;
            } else if (currentY < keepYLevel) {
                mc.thePlayer.motionY = 0.1;
            } else {
                mc.thePlayer.motionY = 0;
            }
        }

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
            }
        }

        if (getSetting("SafeWalk").getValue().equals("On")) {
            if (mc.thePlayer.onGround && mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
                mc.thePlayer.motionX *= 0.5;
                mc.thePlayer.motionZ *= 0.5;
            }
        }

        boolean needsPlacement = mc.theWorld.getBlockState(below).getBlock() == Blocks.air;

        if (needsPlacement) {
            // Raycast check before placing
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

                if (getSetting("Rotate").getValue().equals("On")) {
                    rotateToBlock(below);
                }

                long placeDelay = (long) getSetting("PlaceDelay").getDoubleValue();
                if (System.currentTimeMillis() - lastPlaceTime >= placeDelay) {
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
        if (moveFix.equals("Silent")) {
            boolean canSprint = mc.thePlayer.moveForward > 0 &&
                                !mc.thePlayer.isSneaking() &&
                                !mc.thePlayer.isCollidedHorizontally &&
                                !needsPlacement;
            mc.thePlayer.setSprinting(canSprint);
            if (needsPlacement) {
                mc.thePlayer.motionX *= 0.6;
                mc.thePlayer.motionZ *= 0.6;
            }
        } else if (moveFix.equals("Strict")) {
            mc.thePlayer.setSprinting(false);
            if (needsPlacement) {
                mc.thePlayer.motionX *= 0.3;
                mc.thePlayer.motionZ *= 0.3;
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
                // Check within reach distance (4.5 blocks)
                double dist = mc.thePlayer.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                return dist <= 4.5;
            case "Legit":
                // Check distance and line of sight
                dist = mc.thePlayer.getDistance(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                if (dist > 4.5) return false;
                Vec3 eyes = mc.thePlayer.getPositionEyes(1.0F);
                Vec3 targetVec = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                return mc.theWorld.rayTraceBlocks(eyes, targetVec, false, true, false) == null;
            case "Advanced":
                // Ray trace to block side, require hitting the block
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

    private void rotateToBlock(BlockPos pos) {
        double dx = pos.getX() + 0.5 - mc.thePlayer.posX;
        double dy = pos.getY() + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double dz = pos.getZ() + 0.5 - mc.thePlayer.posZ;
        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(dy, dist) * 180.0 / Math.PI);

        String raycast = getSetting("Raycast").getValue();
        float smoothFactor = 1.0F;
        if (raycast.equals("Legit")) smoothFactor = 0.3F;
        else if (raycast.equals("Advanced")) smoothFactor = 0.6F;
        else if (raycast.equals("Instant")) smoothFactor = 1.0F;

        mc.thePlayer.rotationYaw += (yaw - mc.thePlayer.rotationYaw) * smoothFactor;
        mc.thePlayer.rotationPitch += (pitch - mc.thePlayer.rotationPitch) * smoothFactor;
        mc.thePlayer.prevRotationYaw = mc.thePlayer.rotationYaw;
        mc.thePlayer.prevRotationPitch = mc.thePlayer.rotationPitch;
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
        if (prevSlot != -1) {
            mc.thePlayer.inventory.currentItem = prevSlot;
            prevSlot = -1;
        }
        blocksSinceJump = 0;
        keepYInitialized = false;
    }
}
