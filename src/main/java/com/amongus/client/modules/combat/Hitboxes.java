package com.amongus.client.modules.combat;
import com.amongus.client.modules.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class Hitboxes extends Module {
    public Hitboxes() {
        super("Hitboxes", Keyboard.KEY_NONE, Category.COMBAT, "Expands hitboxes.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Expansion", 0.1, 1.0, 0.3, 0.1));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        String mode = getSetting("Mode").getValue();
        if (mode.equals("None")) return;
        double expansion = getSetting("Expansion").getDoubleValue();
        for (EntityLivingBase entity : mc.theWorld.playerEntities) {
            if (entity != mc.thePlayer && !entity.isDead) {
                entity.getEntityBoundingBox().expand(expansion, expansion, expansion);
            }
        }
    }
}