package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoArmor extends Module {
    private long lastEquip = 0;
    public AutoArmor() {
        super("AutoArmor", Keyboard.KEY_NONE, Category.PLAYER, "Equips best armor automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Delay", 100, 1000, 300, 50));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (System.currentTimeMillis() - lastEquip < (int) getSetting("Delay").getDoubleValue()) return;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();
                int slot = 3 - armor.armorType;
                ItemStack current = mc.thePlayer.inventory.armorInventory[slot];
                if (current == null || (armor.damageReduceAmount > ((ItemArmor) current.getItem()).damageReduceAmount)) {
                    mc.playerController.windowClick(0, 8 - slot, 0, 0, mc.thePlayer);
                    mc.playerController.windowClick(0, i < 9 ? i + 36 : i, 0, 0, mc.thePlayer);
                    mc.playerController.windowClick(0, 8 - slot, 0, 0, mc.thePlayer);
                    lastEquip = System.currentTimeMillis();
                    return;
                }
            }
        }
    }
}