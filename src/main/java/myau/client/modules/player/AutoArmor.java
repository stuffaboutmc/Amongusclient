package myau.client.modules.player;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoArmor extends Module {
    private int tickCounter = 0;

    public AutoArmor() {
        super("AutoArmor", "Auto equips best armor from inventory", Category.PLAYER, Keyboard.KEY_NONE);
    }

    @Override
    public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        tickCounter++;
        if (tickCounter < 5) return;
        tickCounter = 0;

        for (int armorType = 0; armorType < 4; armorType++) {
            ItemStack currentArmor = mc.thePlayer.inventory.armorInventory[armorType];
            int currentProtection = getProtection(currentArmor);

            int bestSlot = -1;
            int bestProtection = currentProtection;

            for (int i = 9; i < 36; i++) {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack != null && stack.getItem() instanceof ItemArmor) {
                    ItemArmor armor = (ItemArmor) stack.getItem() ;
                    if (armor.armorType == armorType) {
                        int prot = getProtection(stack);
                        if (prot > bestProtection) {
                            bestProtection = prot;
                            bestSlot = i;
                        }
                    }
                }
            }

            if (bestSlot != -1) {
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSlot, 0, 1, mc.thePlayer);
            }
        }
    }

    private int getProtection(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemArmor)) return -1;
        ItemArmor armor = (ItemArmor) stack.getItem();
        return armor.damageReduceAmount;
    }
}
