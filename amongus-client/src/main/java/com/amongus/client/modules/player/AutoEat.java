package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoEat extends Module {
    private long lastEat = 0;
    public AutoEat() {
        super("AutoEat", Keyboard.KEY_NONE, Category.PLAYER, "Eats food when hungry.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Hunger", 1, 20, 14, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.getFoodStats().getFoodLevel() > (int) getSetting("Hunger").getDoubleValue()) return;
        if (System.currentTimeMillis() - lastEat < 1000) return;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemFood) {
                int prev = mc.thePlayer.inventory.currentItem;
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(i));
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(stack));
                mc.thePlayer.setItemInUse(stack, 32);
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prev));
                lastEat = System.currentTimeMillis();
                break;
            }
        }
    }
}