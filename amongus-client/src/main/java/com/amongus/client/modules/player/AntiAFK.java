package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AntiAFK extends Module {
    private long lastAction = 0;
    public AntiAFK() {
        super("AntiAFK", Keyboard.KEY_NONE, Category.PLAYER, "Prevents AFK kick.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Interval", 1000, 60000, 30000, 1000));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (System.currentTimeMillis() - lastAction > (int) getSetting("Interval").getDoubleValue()) {
            mc.thePlayer.jump();
            lastAction = System.currentTimeMillis();
        }
    }
}