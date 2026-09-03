package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.lang.reflect.Field;
public class FastEat extends Module {
    public FastEat() {
        super("FastEat", Keyboard.KEY_NONE, Category.MISC, "Eats food instantly.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
            try {
                Field f = net.minecraft.entity.player.EntityPlayer.class.getDeclaredField("itemInUseCount");
                f.setAccessible(true);
                f.setInt(mc.thePlayer, 1);
            } catch (Exception e) {}
        }
    }
}
