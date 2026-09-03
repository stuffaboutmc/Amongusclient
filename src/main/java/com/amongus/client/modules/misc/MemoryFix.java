package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class MemoryFix extends Module {
    private long lastClean = 0;
    public MemoryFix() {
        super("MemoryFix", Keyboard.KEY_NONE, Category.MISC, "Clears memory periodically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (System.currentTimeMillis() - lastClean > 60000) {
            System.gc();
            lastClean = System.currentTimeMillis();
        }
    }
}