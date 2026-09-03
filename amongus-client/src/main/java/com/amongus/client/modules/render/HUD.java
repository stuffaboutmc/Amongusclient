package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class HUD extends Module {
    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.RENDER, "Shows client HUD.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        mc.fontRendererObj.drawStringWithShadow("Amongus Client", 5, 5, new Color(255, 0, 0).getRGB());
        mc.fontRendererObj.drawStringWithShadow("FPS: " + mc.debug.split(" ")[0], 5, 15, new Color(255, 255, 255).getRGB());
    }
}