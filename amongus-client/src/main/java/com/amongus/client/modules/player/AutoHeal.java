package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoHeal extends Module {
    public AutoHeal() {
        super("AutoHeal", Keyboard.KEY_NONE, Category.PLAYER, "Heals automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Health", 1, 20, 14, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.getHealth() < getSetting("Health").getDoubleValue()) {
            if (mc.thePlayer.ticksExisted % 5 == 0) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
            }
        }
    }
}