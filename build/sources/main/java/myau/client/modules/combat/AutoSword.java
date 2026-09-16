package myau.client.modules.combat;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoSword extends Module {
    public AutoSword() {
        super("AutoSword", "Switches to best sword in hotbar", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        int bestSlot = -1;
        float bestDamage = 0;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemSword) {
                ItemSword sword = (ItemSword) stack.getItem();
                float damage = sword.getDamageVsEntity();
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestSlot = i;
                }
            }
        }

        if (bestSlot != -1 && mc.thePlayer.inventory.currentItem != bestSlot) {
            mc.thePlayer.inventory.currentItem = bestSlot;
        }
    }
}
