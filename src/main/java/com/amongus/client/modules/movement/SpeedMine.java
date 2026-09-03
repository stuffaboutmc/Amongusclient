package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
public class SpeedMine extends Module {
    public SpeedMine() {
        super("SpeedMine", Keyboard.KEY_NONE, Category.MOVEMENT, "Mine faster while moving.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (Mouse.isButtonDown(0) && mc.objectMouseOver != null) {
            mc.thePlayer.motionX *= 0.5;
            mc.thePlayer.motionZ *= 0.5;
        }
    }
}