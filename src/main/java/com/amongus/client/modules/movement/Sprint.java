package com.amongus.client.modules.movement;

import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT, "Auto sprint.");
        addSetting(new Setting("Mode", new String[]{"None", "Legit", "Vanilla"}, "Legit"));
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity = mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (mode.equals("Legit")) {
            if (mc.thePlayer.moveForward > 0 && mc.thePlayer.isCollidedHorizontally) mc.thePlayer.setSprinting(true);
        } else if (mode.equals("Vanilla")) {
            if (mc.thePlayer.moveForward > 0 || mc.thePlayer.moveStrafing > 0) mc.thePlayer.setSprinting(true);
        }
    }
}
