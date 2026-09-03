package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HUD extends Module {
    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.RENDER, "On-screen info panel.");
        addSetting(new Setting("Watermark", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("WatermarkText", new String[]{"Amongus","Augustus","Custom"}, "Amongus"));
        addSetting(new Setting("Coordinates", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("FPS", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Ping", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Time", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Direction", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("WatermarkX", 1, 200, 5, 1));
        addSetting(new Setting("WatermarkY", 1, 100, 5, 1));
        addSetting(new Setting("InfoX", 1, 200, 5, 1));
        addSetting(new Setting("InfoY", 1, 100, 25, 1));
        addSetting(new Setting("Red", 0, 255, 255, 1));
        addSetting(new Setting("Green", 0, 255, 0, 1));
        addSetting(new Setting("Blue", 0, 255, 0, 1));
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;

        int red = (int) getSetting("Red").getDoubleValue();
        int green = (int) getSetting("Green").getDoubleValue();
        int blue = (int) getSetting("Blue").getDoubleValue();
        Color accent = new Color(red, green, blue, 255);

        // Watermark - top left, imposter red default
        if (getSetting("Watermark").getValue().equals("On")) {
            int wx = (int) getSetting("WatermarkX").getDoubleValue();
            int wy = (int) getSetting("WatermarkY").getDoubleValue();
            String name = getSetting("WatermarkText").getValue();
            if (name.equals("Custom")) name = "Amongus";
            mc.fontRendererObj.drawStringWithShadow(name, wx + 1, wy + 1, new Color(0, 0, 0, 120).getRGB());
            mc.fontRendererObj.drawStringWithShadow(name, wx, wy, accent.getRGB());
        }

        // Info panel
        int ix = (int) getSetting("InfoX").getDoubleValue();
        int iy = (int) getSetting("InfoY").getDoubleValue();
        int lineHeight = 10;
        int currentY = iy;

        if (getSetting("Coordinates").getValue().equals("On")) {
            String coords = "XYZ: " + (int)mc.thePlayer.posX + " " + (int)mc.thePlayer.posY + " " + (int)mc.thePlayer.posZ;
            mc.fontRendererObj.drawStringWithShadow(coords, ix, currentY, new Color(255, 255, 255, 255).getRGB());
            currentY += lineHeight;
        }

        if (getSetting("FPS").getValue().equals("On")) {
            String fps = "FPS: " + Minecraft.getDebugFPS();
            mc.fontRendererObj.drawStringWithShadow(fps, ix, currentY, new Color(255, 255, 255, 255).getRGB());
            currentY += lineHeight;
        }

        if (getSetting("Ping").getValue().equals("On")) {
            int ping = 0;
            if (mc.getNetHandler() != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null) {
                ping = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
            }
            mc.fontRendererObj.drawStringWithShadow("Ping: " + ping + "ms", ix, currentY, new Color(255, 255, 255, 255).getRGB());
            currentY += lineHeight;
        }

        if (getSetting("Time").getValue().equals("On")) {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            mc.fontRendererObj.drawStringWithShadow(time, ix, currentY, new Color(255, 255, 255, 255).getRGB());
            currentY += lineHeight;
        }

        if (getSetting("Direction").getValue().equals("On")) {
            String dir = getDirection();
            mc.fontRendererObj.drawStringWithShadow(dir, ix, currentY, new Color(255, 255, 255, 255).getRGB());
        }
    }

    private String getDirection() {
        float yaw = mc.thePlayer.rotationYaw % 360;
        if (yaw < 0) yaw += 360;
        if (yaw > 315 || yaw <= 45) return "Facing: South";
        if (yaw > 45 && yaw <= 135) return "Facing: West";
        if (yaw > 135 && yaw <= 225) return "Facing: North";
        return "Facing: East";
    }
}
