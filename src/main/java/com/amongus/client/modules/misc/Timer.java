package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.lang.reflect.Field;
public class Timer extends Module {
    public Timer() {
        super("Timer", Keyboard.KEY_NONE, Category.MISC, "Changes game speed.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Speed", 0.5, 5, 1, 0.1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        try {
            Field f = mc.getClass().getDeclaredField("timer");
            f.setAccessible(true);
            net.minecraft.util.Timer timer = (net.minecraft.util.Timer) f.get(mc);
            timer.timerSpeed = (float) getSetting("Speed").getDoubleValue();
        } catch (Exception e) {}
    }
    @Override
    public void onDisable() {
        try {
            Field f = mc.getClass().getDeclaredField("timer");
            f.setAccessible(true);
            net.minecraft.util.Timer timer = (net.minecraft.util.Timer) f.get(mc);
            timer.timerSpeed = 1.0F;
        } catch (Exception e) {}
    }
}