package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.lang.reflect.Field;
public class FastUse extends Module {
    public FastUse() {
        super("FastUse", Keyboard.KEY_NONE, Category.PLAYER, "Use items faster.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.isUsingItem()) {
            try {
                Field f = net.minecraft.entity.player.EntityPlayer.class.getDeclaredField("itemInUseCount");
                f.setAccessible(true);
                f.setInt(mc.thePlayer, Math.min((int)f.getInt(mc.thePlayer), 5));
            } catch (Exception e) {}
        }
    }
}
