package com.amongus.client.modules.render;

import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HUD extends Module {
    private static final ResourceLocation IMPOSTER = new ResourceLocation("amongus", "textures/imposter.png");

    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.RENDER, "On-screen info panel.");
        addSetting(new Setting("Watermark", new String[]{"Off","Text","Image","Both"}, "Text"));
        addSetting(new Setting("WatermarkText", new String[]{"Amongus","Augustus","Custom"}, "Amongus"));
        addSetting(new Setting("ImageSize", 10, 100, 32, 2));
        addSetting(new Setting("Coordinates", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("FPS", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Ping", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Time", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Direction", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("WatermarkX", 1, 200, 5, 1));
        addSetting(new Setting("WatermarkY", 1, 100, 5, 1));
        addSetting(new Setting("InfoX", 1, 200, 5, 1));
        addSetting(new Setting("InfoY", 1, 100, 25, 1));
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;

        String watermarkMode = getSetting("Watermark").getValue();
        int wx = (int) getSetting("WatermarkX").getDoubleValue();
        int wy = (int) getSetting("WatermarkY").getDoubleValue();
        int imageSize = (int) getSetting("ImageSize").getDoubleValue();

        // Draw watermark
        if (watermarkMode.equals("Text") || watermarkMode.equals("Both")) {
            String name = getSetting("WatermarkText").getValue();
            if (name.equals("Custom")) name = "Amongus";
            mc.fontRendererObj.drawStringWithShadow(name, wx + 1, wy + 1, new Color(0, 0, 0, 120).getRGB());
            mc.fontRendererObj.drawStringWithShadow(name, wx, wy, new Color(255, 0, 0, 255).getRGB());
        }

        if (watermarkMode.equals("Image") || watermarkMode.equals("Both")) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            mc.getTextureManager().bindTexture(IMPOSTER);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GuiScreen.drawModalRectWithCustomSizedTexture(wx, wy, 0, 0, imageSize, imageSize, imageSize, imageSize);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        int ix = (int) getSetting("InfoX").getDoubleValue();
        int iy = (int) getSetting("InfoY").getDoubleValue();
        int currentY = iy;
        int lineHeight = 10;

        if (getSetting("Coordinates").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow("XYZ: " + (int)mc.thePlayer.posX + " " + (int)mc.thePlayer.posY + " " + (int)mc.thePlayer.posZ, ix, currentY, -1);
            currentY += lineHeight;
        }

        if (getSetting("FPS").getValue().equals("On")) {
            String fps = "FPS: " + mc.debug.split(" ")[0];
            mc.fontRendererObj.drawStringWithShadow(fps, ix, currentY, -1);
            currentY += lineHeight;
        }

        if (getSetting("Ping").getValue().equals("On")) {
            int ping = 0;
            if (mc.getNetHandler() != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null) {
                ping = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
            }
            mc.fontRendererObj.drawStringWithShadow("Ping: " + ping + "ms", ix, currentY, -1);
            currentY += lineHeight;
        }

        if (getSetting("Time").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow(new SimpleDateFormat("HH:mm:ss").format(new Date()), ix, currentY, -1);
            currentY += lineHeight;
        }

        if (getSetting("Direction").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow(getDirection(), ix, currentY, -1);
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
