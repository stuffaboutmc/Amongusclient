package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class NoSlowdown extends Module {
    public NoSlowdown() {
        super("NoSlowdown", Keyboard.KEY_NONE, Category.MOVEMENT, "No slowdown when using items.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.isUsingItem() && mc.thePlayer.isSprinting()) {
            mc.thePlayer.setSprinting(true);
        }
    }
}