package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class ItemPhysics extends Module {
    public ItemPhysics() {
        super("ItemPhysics", Keyboard.KEY_NONE, Category.MISC, "Items float on water.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof EntityItem) {
                EntityItem item = (EntityItem) obj;
                if (item.isInWater()) {
                    item.motionY = 0.1;
                }
            }
        }
    }
}