package com.amongus.client.modules.render;
import com.amongus.client.modules.Module;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
public class Coordinates extends Module {
    public Coordinates() {
        super("Coordinates", Keyboard.KEY_NONE, Category.RENDER, "Shows position.");
        addSetting(new Setting("Mode", new String[]{"None","Basic","Advanced"}, "Basic"));
    }
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (getSetting("Mode").getValue().equals("None")) return;
        if (mc.thePlayer == null) return;
        String coords = "X: " + (int)mc.thePlayer.posX + " Y: " + (int)mc.thePlayer.posY + " Z: " + (int)mc.thePlayer.posZ;
        mc.fontRendererObj.drawStringWithShadow(coords, 5, 50, new Color(255, 255, 255).getRGB());
    }
}