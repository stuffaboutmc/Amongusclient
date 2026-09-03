package com.amongus.client.modules.misc;
import com.amongus.client.modules.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
public class AutoReconnect extends Module {
    private long lastReconnect = 0;
    public AutoReconnect() {
        super("AutoReconnect", Keyboard.KEY_NONE, Category.MISC, "Reconnects automatically.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
        addSetting(new Setting("Delay", 1000, 10000, 3000, 500));
    }
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entity != mc.thePlayer) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.getNetHandler() == null && System.currentTimeMillis() - lastReconnect > (int) getSetting("Delay").getDoubleValue()) {
            mc.displayGuiScreen(new net.minecraft.client.gui.GuiMultiplayer(new net.minecraft.client.gui.GuiMainMenu()));
            lastReconnect = System.currentTimeMillis();
        }
    }
}