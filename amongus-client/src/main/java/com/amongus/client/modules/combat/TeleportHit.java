package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class TeleportHit extends Module {
    public TeleportHit() {
        super("TeleportHit", Keyboard.KEY_NONE, Category.COMBAT, "Hits from any distance.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Range", 3, 20, 10, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        double range = getSetting("Range").getDoubleValue();
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity == mc.thePlayer || entity.isDead || entity.getHealth() <= 0) continue;
            if (mc.thePlayer.getDistanceToEntity(entity) <= range) {
                mc.thePlayer.swingItem();
                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                break;
            }
        }
    }
}