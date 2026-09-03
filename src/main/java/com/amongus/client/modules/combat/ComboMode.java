package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class ComboMode extends Module {
    private long lastCombo = 0;
    public ComboMode() {
        super("ComboMode", Keyboard.KEY_NONE, Category.COMBAT, "Combos enemies.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Hits", 1, 10, 3, 1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
            EntityLivingBase target = (EntityLivingBase) mc.objectMouseOver.entityHit;
            if (target != mc.thePlayer && System.currentTimeMillis() - lastCombo >= 100) {
                int hits = (int) getSetting("Hits").getDoubleValue();
                for (int i = 0; i < hits; i++) {
                    mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                }
                lastCombo = System.currentTimeMillis();
            }
        }
    }
}