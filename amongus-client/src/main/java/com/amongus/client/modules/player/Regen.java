package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Regen extends Module {
    public Regen() {
        super("Regen", Keyboard.KEY_NONE, Category.PLAYER, "Regenerate health.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Speed", 1, 20, 10, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth()) {
            int speed = (int) getSetting("Speed").getDoubleValue();
            if (mc.thePlayer.ticksExisted % speed == 0) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
            }
        }
    }
}