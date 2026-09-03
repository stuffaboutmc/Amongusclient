package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Speed extends Module {
    public Speed() {
        super("Speed", Keyboard.KEY_NONE, Category.MOVEMENT, "Makes you faster.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced","Bhop"}, "Basic"));
        addSetting(new Setting("Multiplier", 1, 10, 2, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0) return;
        double mult = getSetting("Multiplier").getDoubleValue();
        mc.thePlayer.motionX *= mult;
        mc.thePlayer.motionZ *= mult;
    }
}
