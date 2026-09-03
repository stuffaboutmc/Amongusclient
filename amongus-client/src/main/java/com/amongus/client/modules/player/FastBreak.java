package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.lang.reflect.Field;
public class FastBreak extends Module {
    public FastBreak() {
        super("FastBreak", Keyboard.KEY_NONE, Category.PLAYER, "Break blocks faster.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        try {
            Field f = mc.getClass().getDeclaredField("leftClickCounter");
            f.setAccessible(true);
            f.setInt(mc, 0);
        } catch (Exception e) {}
    }
}