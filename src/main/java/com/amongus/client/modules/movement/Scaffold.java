package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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

        // KeepY initialization
        if (getSetting("KeepY").getValue().equals("On")) {
            if (!keepYInitialized) {
                keepYLevel = Math.floor(mc.thePlayer.posY);
                keepYInitialized = true;
            }
        } else {
            keepYInitialized = false;
        }

        // AutoJump handling with KeepY conflict
        if (getSetting("AutoJump").getValue().equals("On")) {
            if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking()) {
                if (getSetting("KeepY").getValue().equals("On")) {
                    mc.thePlayer.motionY = 0;
                } else {
                    mc.thePlayer.jump();
                }
            }
        }

        // KeepY: enforce exact Y level
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
            }
        }

        // SafeWalk
        if (getSetting("SafeWalk").getValue().equals("On")) {
            if (mc.thePlayer.onGround && mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
                mc.thePlayer.motionX *= 0.5;
                mc.thePlayer.motionZ *= 0.5;
            }
        }

        // Place block if air
        if (mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
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

            // Rotate toward block (visible)
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
        } else {
            if (getSetting("AutoSwitch").getValue().equals("On") && prevSlot != -1) {
                mc.thePlayer.inventory.currentItem = prevSlot;
                prevSlot = -1;
            }
        }

        // MoveFix - more natural
        String moveFix = getSetting("MoveFix").getValue();
        if (moveFix.equals("Silent")) {
            // Recreate real player movement: preserve forward momentum, reduce strafing
            float forward = mc.thePlayer.moveForward;
            float strafe = mc.thePlayer.moveStrafing;
            double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
            double forwardX = -Math.sin(yaw) * forward;
            double forwardZ = Math.cos(yaw) * forward;
            double strafeX = Math.cos(yaw) * strafe;
            double strafeZ = Math.sin(yaw) * strafe;
            double speed = 0.15; // normal walking speed factor
            mc.thePlayer.motionX = forwardX * speed + strafeX * speed * 0.5;
            mc.thePlayer.motionZ = forwardZ * speed + strafeZ * speed * 0.5;
        } else if (moveFix.equals("Strict")) {
            // Almost stop all motion for careful bridging
            mc.thePlayer.motionX *= 0.1;
            mc.thePlayer.motionZ *= 0.1;
            mc.thePlayer.setSprinting(false);
        }
        // None: no adjustments
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
