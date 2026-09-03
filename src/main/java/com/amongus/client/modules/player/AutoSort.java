package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoSort extends Module {
    private long lastSort = 0;
    public AutoSort() {
        super("AutoSort", Keyboard.KEY_NONE, Category.PLAYER, "Sorts inventory.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Delay", 1000, 10000, 5000, 1000));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (System.currentTimeMillis() - lastSort < (int) getSetting("Delay").getDoubleValue()) return;
        for (int i = 9; i < 36; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null) {
                for (int j = i + 1; j < 36; j++) {
                    ItemStack other = mc.thePlayer.inventory.getStackInSlot(j);
                    if (other != null && other.getItem() == stack.getItem()) {
                        mc.playerController.windowClick(0, i, 0, 0, mc.thePlayer);
                        mc.playerController.windowClick(0, j, 0, 0, mc.thePlayer);
                        mc.playerController.windowClick(0, i, 0, 0, mc.thePlayer);
                        lastSort = System.currentTimeMillis();
                        return;
                    }
                }
            }
        }
    }
}