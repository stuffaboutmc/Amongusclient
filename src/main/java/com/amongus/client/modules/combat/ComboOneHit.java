package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class ComboOneHit extends Module {
    public ComboOneHit() {
        super("ComboOneHit", Keyboard.KEY_NONE, Category.COMBAT, "Rapid combo attacks.");
        addSetting(new Setting("Hits", 1, 10, 3, 1));
        addSetting(new Setting("Delay", 0, 500, 0, 10));
        addSetting(new Setting("OnlyPlayers", new String[]{"Off","On"}, "On"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            EntityLivingBase target = (EntityLivingBase) mc.objectMouseOver.entityHit;
            if (target != mc.thePlayer && mc.thePlayer.hurtTime == 0) {
                int hits = (int) getSetting("Hits").getDoubleValue();
                for (int i = 0; i < hits; i++) {
                    mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                }
            }
        }
    }
}