package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.lang.reflect.Field;
public class FastPlace extends Module {
    public FastPlace() {
        super("FastPlace", Keyboard.KEY_NONE, Category.PLAYER, "Place blocks faster.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        try {
            Field f = mc.getClass().getDeclaredField("rightClickDelayTimer");
            f.setAccessible(true);
            f.setInt(mc, 0);
        } catch (Exception e) {}
    }
}