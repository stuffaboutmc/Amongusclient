package myau.client.module.impl;

import myau.client.module.Module;
import myau.client.settings.Setting;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class Scaffold extends Module {

    private Setting mode, range, swing;

    public Scaffold() {
        super("Scaffold", "Movement");
    }

    @Override
    public void setupSettings() {
        mode = new Setting("Mode", "Normal", "Fast", "Legit");
        range = new Setting("Range", 4.0, 1.0, 6.0, 0.1);
        swing = new Setting("Swing", true);
        addSetting(mode);
        addSetting(range);
        addSetting(swing);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        if (mc.theWorld.isAirBlock(pos)) {
            int slot = getBlockSlot();
            if (slot == -1) return;

            EnumFacing side = getPlaceSide(pos);
            if (side == null) return;

            int oldSlot = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = slot;

            mc.playerController.clickBlock(pos, side);
            if (swing.getBooleanValue()) {
                mc.thePlayer.swingItem();
            }

            mc.thePlayer.inventory.currentItem = oldSlot;
        }
    }

    private int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }

    private EnumFacing getPlaceSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);
            if (!mc.theWorld.isAirBlock(neighbor)) {
                return side.getOpposite();
            }
        }
        return null;
    }
}
