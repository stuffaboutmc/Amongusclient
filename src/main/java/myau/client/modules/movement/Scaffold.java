package myau.client.modules.movement;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {
    private int tickCounter = 0;

    public Scaffold() {
        super("Scaffold", "Auto places blocks under feet", Category.MOVEMENT, Keyboard.KEY_NONE);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        tickCounter++;
        if (tickCounter < 1) return;
        tickCounter = 0;

        BlockPos below = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
        if (mc.theWorld.getBlockState(below).getBlock() != Blocks.air) return;

        int slot = findBlock();
        if (slot == -1) return;

        int oldSlot = mc.thePlayer.inventory.currentItem;
        mc.thePlayer.inventory.currentItem = slot;
        try {
            java.lang.reflect.Field f = Minecraft.class.getDeclaredField("rightClickDelayTimer");
            f.setAccessible(true);
            f.setInt(mc, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ItemStack stack = mc.thePlayer.inventory.getCurrentItem();
        mc.playerController.func_178890_a(
            mc.thePlayer, mc.theWorld, stack,
            mc.objectMouseOver.getBlockPos(),
            mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec
        );
        mc.thePlayer.inventory.currentItem = oldSlot;
    }

    private int findBlock() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                Block block = Block.getBlockFromItem(stack.getItem());
                if (block != null && block != Blocks.air && block.isFullBlock()) {
                    return i;
                }
            }
        }
        return -1;
    }
}
