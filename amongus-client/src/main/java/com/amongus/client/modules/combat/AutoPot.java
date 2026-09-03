package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoPot extends Module {
    private long lastPot = 0;
    public AutoPot() {
        super("AutoPot", Keyboard.KEY_NONE, Category.COMBAT, "Throws potions when low health.");
        addSetting(new Setting("Health", 1, 20, 14, 1));
        addSetting(new Setting("Delay", 100, 2000, 1000, 50));
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (mc.thePlayer.getHealth() > getSetting("Health").getDoubleValue()) return;
        if (System.currentTimeMillis() - lastPot < (int) getSetting("Delay").getDoubleValue()) return;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemPotion) {
                int prev = mc.thePlayer.inventory.currentItem;
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(i));
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(stack));
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prev));
                lastPot = System.currentTimeMillis();
                break;
            }
        }
    }
}