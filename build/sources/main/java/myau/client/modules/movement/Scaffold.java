package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {

    public Scaffold() {
        super("Scaffold", "Places blocks under you", Category.MOVEMENT, Keyboard.KEY_NONE);
        addSetting(new Setting("Mode", SettingType.MODE, "Normal", "Normal", "Fast", "Legit"));
        addSetting(new Setting("Range", SettingType.NUMBER, 4.0, 1.0, 6.0));
        addSetting(new Setting("Swing", SettingType.BOOLEAN, true));
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
            Setting swingSetting = getSetting("Swing");
            if (swingSetting != null && swingSetting.getBoolean()) {
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
