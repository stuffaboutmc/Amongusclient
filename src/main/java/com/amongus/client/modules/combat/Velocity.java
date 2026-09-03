package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Keyboard.KEY_NONE, Category.COMBAT, "Reduces knockback.");
        addSetting(new Setting("Horizontal", 0, 100, 0, 1));
        addSetting(new Setting("Vertical", 0, 100, 0, 1));
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.hurtTime > 0 && mc.thePlayer.hurtTime < 10) {
            double h = getSetting("Horizontal").getDoubleValue() / 100.0;
            double v = getSetting("Vertical").getDoubleValue() / 100.0;
            mc.thePlayer.motionX *= (1.0 - h);
            mc.thePlayer.motionZ *= (1.0 - h);
            mc.thePlayer.motionY *= (1.0 - v);
        }
    }
}
