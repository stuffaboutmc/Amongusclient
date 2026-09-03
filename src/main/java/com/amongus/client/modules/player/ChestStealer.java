package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class ChestStealer extends Module {
    private long lastSteal = 0;
    public ChestStealer() {
        super("ChestStealer", Keyboard.KEY_NONE, Category.PLAYER, "Takes items from chests.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Delay", 50, 500, 100, 10));
    }
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) {
            lastSteal = System.currentTimeMillis();
        }
    }
    @SubscribeEvent
    public void onLivingUpdate(net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (!(mc.currentScreen instanceof GuiChest)) return;
        if (System.currentTimeMillis() - lastSteal < (int) getSetting("Delay").getDoubleValue()) return;
        GuiChest chest = (GuiChest) mc.currentScreen;
        for (int i = 0; i < chest.inventorySlots.inventorySlots.size() - 36; i++) {
            ItemStack stack = chest.inventorySlots.getSlot(i).getStack();
            if (stack != null) {
                mc.playerController.windowClick(chest.inventorySlots.windowId, i, 0, 1, mc.thePlayer);
                lastSteal = System.currentTimeMillis();
                return;
            }
        }
    }
}