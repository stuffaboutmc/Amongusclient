package com.amongus.client.modules.player;
import com.amongus.client.modules.Module;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoLoot extends Module {
    public AutoLoot() {
        super("AutoLoot", Keyboard.KEY_NONE, Category.PLAYER, "Picks up items automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Range", 1, 10, 3, 0.5));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        double range = getSetting("Range").getDoubleValue();
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityItem) {
                EntityItem item = (EntityItem) obj;
                if (mc.thePlayer.getDistanceToEntity(item) < range) {
                    item.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                }
            }
        }
    }
}