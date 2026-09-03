package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoDisconnect extends Module {
    public AutoDisconnect() {
        super("AutoDisconnect", Keyboard.KEY_NONE, Category.PLAYER, "Disconnects at low health.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Health", 1, 20, 4, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.getHealth() <= getSetting("Health").getDoubleValue()) {
            mc.theWorld.sendQuittingDisconnectingPacket();
        }
    }
}