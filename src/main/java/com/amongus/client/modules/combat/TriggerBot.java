package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class TriggerBot extends Module {
    private long lastAttack = 0;
    public TriggerBot() {
        super("TriggerBot", Keyboard.KEY_NONE, Category.COMBAT, "Attacks what you look at.");
        addSetting(new Setting("CPS", 1, 20, 10, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        int cps = (int) getSetting("CPS").getDoubleValue();
        long delay = 1000 / cps;
        if (System.currentTimeMillis() - lastAttack < delay) return;
        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            EntityLivingBase target = (EntityLivingBase) mc.objectMouseOver.entityHit;
            if (target != mc.thePlayer && !target.isDead && target.getHealth() > 0) {
                mc.thePlayer.swingItem();
                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                lastAttack = System.currentTimeMillis();
            }
        }
    }
}
