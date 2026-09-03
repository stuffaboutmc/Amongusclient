package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {
    private int blocksSinceJump = 0;
    private long lastPlaceTime = 0;
    private int prevSlot = -1;

    public Scaffold() {
        super("Scaffold", Keyboard.KEY_NONE, Category.MOVEMENT, "Places blocks under you with visible rotation.");
        addSetting(new Setting("Mode", new String[]{"None","Telly","Normal","Godbridge","Snap"}, "Normal"));
        addSetting(new Setting("Rotate", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Tower", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("PlaceDelay", 0, 500, 0, 10));
        addSetting(new Setting("AutoJump", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("KeepY", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("AutoSwitch", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("SafeWalk", new String[]{"Off","On"}, "Off"));
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;

        // AutoJump
        if (getSetting("AutoJump").getValue().equals("On")) {
            if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking()) {
                mc.thePlayer.jump();
            }
        }

        // KeepY
        if (getSetting("KeepY").getValue().equals("On")) {
            BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
            if (mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
                mc.thePlayer.motionY = 0.1;
            }
        }

        BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

        // SafeWalk
        if (getSetting("SafeWalk").getValue().equals("On")) {
            if (mc.thePlayer.onGround && mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
                mc.thePlayer.motionX *= 0.5;
                mc.thePlayer.motionZ *= 0.5;
            }
        }

        if (mc.theWorld.getBlockState(below).getBlock() == Blocks.air) {
            if (mode.equals("Godbridge")) {
                blocksSinceJump++;
                if (blocksSinceJump >= 9) {
                    mc.thePlayer.jump();
                    blocksSinceJump = 0;
                    return;
                }
            }

            if (mode.equals("Tower") && getSetting("Tower").getValue().equals("On")) {
                mc.thePlayer.motionY = 0.42;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
            }

            // Rotate toward the block below
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
    }

    private void rotateToBlock(BlockPos pos) {
        double dx = pos.getX() + 0.5 - mc.thePlayer.posX;
        double dy = pos.getY() + 0.5 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double dz = pos.getZ() + 0.5 - mc.thePlayer.posZ;
        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(dy, dist) * 180.0 / Math.PI);

        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
        mc.thePlayer.prevRotationYaw = yaw;
        mc.thePlayer.prevRotationPitch = pitch;
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
    }
}
