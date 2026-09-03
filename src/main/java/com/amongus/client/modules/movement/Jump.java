package com.amongus.client.modules.movement;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Jump extends Module {
    private int tickCounter = 0;
    public Jump() {
        super("Jump", Keyboard.KEY_NONE, Category.MOVEMENT, "Jumps every X ticks.");
        addSetting(new Setting("Enabled", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("Interval", 1, 10, 5, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Enabled").getValue().equals("Off")) return;
        tickCounter++;
        if (tickCounter >= (int) getSetting("Interval").getDoubleValue()) {
            if (mc.thePlayer.onGround) mc.thePlayer.jump();
            tickCounter = 0;
        }
    }
}