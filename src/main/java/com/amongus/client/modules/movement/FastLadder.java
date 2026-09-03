package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class FastLadder extends Module {
    public FastLadder() {
        super("FastLadder", Keyboard.KEY_NONE, Category.MOVEMENT, "Climb ladders faster.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Speed", 1, 10, 5, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.isOnLadder()) {
            mc.thePlayer.motionY = getSetting("Speed").getDoubleValue() * 0.1;
        }
    }
}