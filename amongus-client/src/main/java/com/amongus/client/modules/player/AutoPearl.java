package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoPearl extends Module {
    private long lastPearl = 0;
    public AutoPearl() {
        super("AutoPearl", Keyboard.KEY_NONE, Category.PLAYER, "Throws pearl automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (System.currentTimeMillis() - lastPearl < 2000) return;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemEnderPearl) {
                int prev = mc.thePlayer.inventory.currentItem;
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(i));
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(stack));
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(prev));
                lastPearl = System.currentTimeMillis();
                break;
            }
        }
    }
}