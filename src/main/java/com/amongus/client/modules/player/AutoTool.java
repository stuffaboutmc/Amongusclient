package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", Keyboard.KEY_NONE, Category.PLAYER, "Switches to best tool.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;
        BlockPos pos = mc.objectMouseOver.getBlockPos();
        float bestSpeed = 1.0F;
        int bestSlot = mc.thePlayer.inventory.currentItem;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemTool) {
                ItemTool tool = (ItemTool) stack.getItem();
                float speed = tool.getStrVsBlock(stack, mc.theWorld.getBlockState(pos).getBlock());
                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }
        }
        mc.thePlayer.inventory.currentItem = bestSlot;
    }
}