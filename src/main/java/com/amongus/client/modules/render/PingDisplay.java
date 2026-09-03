package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class PingDisplay extends Module {
    public PingDisplay() {
        super("PingDisplay", Keyboard.KEY_NONE, Category.RENDER, "Shows ping.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer == null || mc.getNetHandler() == null) return;
        int ping = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null ? mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() : 0;
        mc.fontRendererObj.drawStringWithShadow("Ping: " + ping + "ms", 5, 70, new Color(255, 200, 50).getRGB());
    }
}