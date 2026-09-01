package com.user.utilitymod.module.modules.misc;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class AutoTool extends Module {

    public AutoTool() {
        super("AutoTool", Category.MISC);
    }

    @Override
    public void onTick() {
        if (!isPlayerValid()) return;
        MovingObjectPosition trace = mc.objectMouseOver;
        if (trace == null || trace.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;

        BlockPos pos = trace.getBlockPos();
        IBlockState state = mc.theWorld.getBlockState(pos);
        Block block = state.getBlock();

        int bestSlot = findBestToolSlot(block, state);
        if (bestSlot != -1) {
            mc.thePlayer.inventory.currentItem = bestSlot;
        }
    }

    private int findBestToolSlot(Block block, IBlockState state) {
        float bestSpeed = -1f;
        int bestSlot = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) continue;

            float speed = stack.getStrVsBlock(block);
            Item item = stack.getItem();
            boolean isTool = item.getToolClasses(stack).size() > 0;

            if (isTool && speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        return bestSlot;
    }
}
