package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class FPSDisplay extends Module {
    public FPSDisplay() {
        super("FPSDisplay", Keyboard.KEY_NONE, Category.RENDER, "Shows FPS.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        String fps = "FPS: " + mc.debug.split(" ")[0];
        mc.fontRendererObj.drawStringWithShadow(fps, 5, 60, new Color(0, 255, 128).getRGB());
    }
}