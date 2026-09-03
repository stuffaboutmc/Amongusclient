package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class InventoryCleaner extends Module {
    private long lastClean = 0;
    public InventoryCleaner() {
        super("InventoryCleaner", Keyboard.KEY_NONE, Category.PLAYER, "Drops unwanted items.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Delay", 500, 5000, 2000, 500));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (System.currentTimeMillis() - lastClean < (int) getSetting("Delay").getDoubleValue()) return;
        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem().getUnlocalizedName().contains("cobblestone")) {
                mc.thePlayer.dropOneItem(true);
                lastClean = System.currentTimeMillis();
                break;
            }
        }
    }
}