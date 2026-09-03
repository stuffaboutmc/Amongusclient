package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class NoVoid extends Module {
    public NoVoid() {
        super("NoVoid", Keyboard.KEY_NONE, Category.PLAYER, "Teleports you back from void.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Threshold", 1, 20, 5, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer.posY < getSetting("Threshold").getDoubleValue()) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, 100, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            mc.thePlayer.setPosition(mc.thePlayer.posX, 100, mc.thePlayer.posZ);
        }
    }
}