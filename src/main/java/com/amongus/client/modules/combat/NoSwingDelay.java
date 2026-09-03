package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.lang.reflect.Field;
public class NoSwingDelay extends Module {
    public NoSwingDelay() {
        super("NoSwingDelay", Keyboard.KEY_NONE, Category.COMBAT, "No swing cooldown.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        try {
            Field f = mc.thePlayer.getClass().getDeclaredField("swingProgressInt");
            f.setAccessible(true);
            f.setInt(mc.thePlayer, 0);
        } catch (Exception e) {}
    }
}