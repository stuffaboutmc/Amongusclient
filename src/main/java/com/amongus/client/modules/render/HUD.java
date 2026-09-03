package com.amongus.client.modules.render;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HUD extends Module {
    private static final ResourceLocation IMPOSTER = new ResourceLocation("amongus", "textures/imposter.png");

    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.RENDER, "On-screen info panel with styled watermark and module list.");
        addSetting(new Setting("Watermark", new String[]{"Off","Text","Image","Both"}, "Text"));
        addSetting(new Setting("WatermarkText", new String[]{"Amongus","Augustus","Custom"}, "Amongus"));
        addSetting(new Setting("ImageSize", 10, 100, 32, 2));
        addSetting(new Setting("WatermarkRed", 0, 255, 255, 1));
        addSetting(new Setting("WatermarkGreen", 0, 255, 0, 1));
        addSetting(new Setting("WatermarkBlue", 0, 255, 0, 1));
        addSetting(new Setting("WatermarkShadow", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("WatermarkOutline", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("WatermarkGlow", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("WatermarkSize", 1, 10, 2, 0.5));
        addSetting(new Setting("Coordinates", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("FPS", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Ping", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Time", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Direction", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("ModuleList", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("ModuleListX", 1, 400, 5, 1));
        addSetting(new Setting("ModuleListY", 1, 200, 40, 1));
        addSetting(new Setting("ModuleListBG", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("BGRed", 0, 255, 10, 1));
        addSetting(new Setting("BGGreen", 0, 255, 10, 1));
        addSetting(new Setting("BGBlue", 0, 255, 10, 1));
        addSetting(new Setting("BGOpacity", 0, 255, 180, 5));
        addSetting(new Setting("TextRed", 0, 255, 230, 1));
        addSetting(new Setting("TextGreen", 0, 255, 230, 1));
        addSetting(new Setting("TextBlue", 0, 255, 235, 1));
        addSetting(new Setting("SortAlphabetical", new String[]{"Off","On"}, "On"));
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

        // Watermark text with style
        if (watermarkMode.equals("Text") || watermarkMode.equals("Both")) {
            String name = getSetting("WatermarkText").getValue();
            if (name.equals("Custom")) name = "Amongus";
            int red = (int) getSetting("WatermarkRed").getDoubleValue();
            int green = (int) getSetting("WatermarkGreen").getDoubleValue();
            int blue = (int) getSetting("WatermarkBlue").getDoubleValue();
            Color color = new Color(red, green, blue, 255);

            float scale = (float) getSetting("WatermarkSize").getDoubleValue();
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, 1.0F);
            int scaledX = (int) (wx / scale);
            int scaledY = (int) (wy / scale);

            // Shadow
            if (getSetting("WatermarkShadow").getValue().equals("On")) {
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY + 1, new Color(0, 0, 0, 180).getRGB());
            }

            // Glow (simulate by drawing multiple times with low alpha around)
            if (getSetting("WatermarkGlow").getValue().equals("On")) {
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX, scaledY - 1, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX, scaledY + 1, new Color(red, green, blue, 60).getRGB());
            }

            // Main text
            mc.fontRendererObj.drawString(name, scaledX, scaledY, color.getRGB());

            // Outline (draw text with offset in black behind)
            if (getSetting("WatermarkOutline").getValue().equals("On")) {
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY - 1, new Color(0, 0, 0, 255).getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY - 1, new Color(0, 0, 0, 255).getRGB());
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY + 1, new Color(0, 0, 0, 255).getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY + 1, new Color(0, 0, 0, 255).getRGB());
                mc.fontRendererObj.drawString(name, scaledX, scaledY, color.getRGB());
            }

            GlStateManager.popMatrix();
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

        // Info panel
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

        // Module list with style
        if (getSetting("ModuleList").getValue().equals("On")) {
            int mlx = (int) getSetting("ModuleListX").getDoubleValue();
            int mly = (int) getSetting("ModuleListY").getDoubleValue();
            List<String> enabledModules = new ArrayList<>();
            for (Module m : AmongusClient.moduleManager.getModules()) {
                if (m.isEnabled()) {
                    enabledModules.add(m.getName());
                }
            }
            if (getSetting("SortAlphabetical").getValue().equals("On")) {
                Collections.sort(enabledModules);
            }

            int textRed = (int) getSetting("TextRed").getDoubleValue();
            int textGreen = (int) getSetting("TextGreen").getDoubleValue();
            int textBlue = (int) getSetting("TextBlue").getDoubleValue();
            Color textColor = new Color(textRed, textGreen, textBlue, 255);

            // Background
            if (getSetting("ModuleListBG").getValue().equals("On")) {
                int bgRed = (int) getSetting("BGRed").getDoubleValue();
                int bgGreen = (int) getSetting("BGGreen").getDoubleValue();
                int bgBlue = (int) getSetting("BGBlue").getDoubleValue();
                int bgOpacity = (int) getSetting("BGOpacity").getDoubleValue();
                int width = 0;
                for (String name : enabledModules) {
                    width = Math.max(width, mc.fontRendererObj.getStringWidth(name));
                }
                int height = enabledModules.size() * 10;
                if (width > 0 && height > 0) {
                    GuiScreen.drawRect(mlx - 2, mly - 2, mlx + width + 4, mly + height + 2, new Color(bgRed, bgGreen, bgBlue, bgOpacity).getRGB());
                }
            }

            int y = mly;
            for (String name : enabledModules) {
                mc.fontRendererObj.drawStringWithShadow(name, mlx, y, textColor.getRGB());
                y += 10;
            }
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
