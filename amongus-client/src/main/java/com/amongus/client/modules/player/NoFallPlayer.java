package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class NoFallPlayer extends Module {
    public NoFallPlayer() {
        super("NoFallPlayer", Keyboard.KEY_NONE, Category.PLAYER, "No fall damage.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.fallDistance > 2.0F) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
            mc.thePlayer.fallDistance = 0;
        }
    }
}