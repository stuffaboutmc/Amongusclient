package com.amongus.client.modules.render;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HUD extends Module {
    private boolean expanded = false;

    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.RENDER, "On-screen info and stunning arraylist.");
        addSetting(new Setting("Watermark", new String[]{"Off","Text","Image","Both"}, "Text"));
        addSetting(new Setting("WatermarkText", new String[]{"Amongus","Augustus","Custom"}, "Amongus"));
        addSetting(new Setting("ImageSize", 10, 100, 32, 2));
        addSetting(new Setting("WatermarkRed", 0, 255, 255, 1));
        addSetting(new Setting("WatermarkGreen", 0, 255, 0, 1));
        addSetting(new Setting("WatermarkBlue", 0, 255, 0, 1));
        addSetting(new Setting("WatermarkShadow", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("WatermarkGlow", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("WatermarkOutline", new String[]{"Off","On"}, "Off"));
        addSetting(new Setting("WatermarkSize", 1, 10, 2, 0.5));
        addSetting(new Setting("ArrayList", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("ArrayListScale", 0.5, 3, 1, 0.1));
        addSetting(new Setting("ArrayListX", 1, 400, 5, 1));
        addSetting(new Setting("ArrayListY", 1, 200, 25, 1));
        addSetting(new Setting("ColorMode", new String[]{"Single","Gradient","Rainbow"}, "Gradient"));
        addSetting(new Setting("SingleRed", 0, 255, 255, 1));
        addSetting(new Setting("SingleGreen", 0, 255, 255, 1));
        addSetting(new Setting("SingleBlue", 0, 255, 255, 1));
        addSetting(new Setting("GradientStartRed", 0, 255, 255, 1));
        addSetting(new Setting("GradientStartGreen", 0, 255, 0, 1));
        addSetting(new Setting("GradientStartBlue", 0, 255, 0, 1));
        addSetting(new Setting("GradientEndRed", 0, 255, 0, 1));
        addSetting(new Setting("GradientEndGreen", 0, 255, 255, 1));
        addSetting(new Setting("GradientEndBlue", 0, 255, 255, 1));
        addSetting(new Setting("RainbowSpeed", 1, 20, 5, 1));
        addSetting(new Setting("ClickToExpand", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("ExpandedSize", 50, 200, 120, 5));
        addSetting(new Setting("ExpandedRed", 0, 255, 20, 1));
        addSetting(new Setting("ExpandedGreen", 0, 255, 20, 1));
        addSetting(new Setting("ExpandedBlue", 0, 255, 20, 1));
        addSetting(new Setting("ExpandedOpacity", 0, 255, 180, 5));
        addSetting(new Setting("Coordinates", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("FPS", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Ping", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Time", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Direction", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("WatermarkX", 1, 400, 5, 1));
        addSetting(new Setting("WatermarkY", 1, 200, 5, 1));
        addSetting(new Setting("InfoX", 1, 400, 5, 1));
        addSetting(new Setting("InfoY", 1, 200, 25, 1));
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;

        int screenWidth = event.resolution.getScaledWidth();
        int screenHeight = event.resolution.getScaledHeight();

        // --- Watermark (top-right) ---
        String watermarkMode = getSetting("Watermark").getValue();
        int wx = screenWidth - 200 + (int)getSetting("WatermarkX").getDoubleValue() - 5;
        int wy = 5 + (int)getSetting("WatermarkY").getDoubleValue() - 5;
        int imageSize = (int) getSetting("ImageSize").getDoubleValue();

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
            int scaledX = (int)(wx / scale);
            int scaledY = (int)(wy / scale);
            if (getSetting("WatermarkShadow").getValue().equals("On"))
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY + 1, new Color(0,0,0,180).getRGB());
            if (getSetting("WatermarkGlow").getValue().equals("On")) {
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX, scaledY - 1, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX, scaledY + 1, new Color(red, green, blue, 60).getRGB());
            }
            mc.fontRendererObj.drawString(name, scaledX, scaledY, color.getRGB());
            if (getSetting("WatermarkOutline").getValue().equals("On")) {
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY - 1, Color.BLACK.getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY - 1, Color.BLACK.getRGB());
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY + 1, Color.BLACK.getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY + 1, Color.BLACK.getRGB());
                mc.fontRendererObj.drawString(name, scaledX, scaledY, color.getRGB());
            }
            GlStateManager.popMatrix();
        }

        if (watermarkMode.equals("Image") || watermarkMode.equals("Both")) {
            GlStateManager.enableBlend();
            mc.getTextureManager().bindTexture(new net.minecraft.util.ResourceLocation("amongus", "textures/imposter.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GuiScreen.drawModalRectWithCustomSizedTexture(wx, wy, 0, 0, imageSize, imageSize, imageSize, imageSize);
            GlStateManager.disableBlend();
        }

        // --- Info panel (top-right below watermark) ---
        int ix = screenWidth - 200 + (int)getSetting("InfoX").getDoubleValue() - 5;
        int iy = 30 + (int)getSetting("InfoY").getDoubleValue() - 25;
        int lineHeight = 10;
        int currentY = iy;
        if (getSetting("Coordinates").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow("XYZ: " + (int)mc.thePlayer.posX + " " + (int)mc.thePlayer.posY + " " + (int)mc.thePlayer.posZ, ix, currentY, -1);
            currentY += lineHeight;
        }
        if (getSetting("FPS").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow("FPS: " + mc.debug.split(" ")[0], ix, currentY, -1);
            currentY += lineHeight;
        }
        if (getSetting("Ping").getValue().equals("On")) {
            int ping = 0;
            if (mc.getNetHandler() != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null)
                ping = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
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

        // --- ArrayList ---
        if (getSetting("ArrayList").getValue().equals("On")) {
            List<String> enabledModules = new ArrayList<>();
            for (Module m : AmongusClient.moduleManager.getModules()) {
                if (m.isEnabled()) enabledModules.add(m.getName());
            }
            Collections.sort(enabledModules);

            float scale = (float) getSetting("ArrayListScale").getDoubleValue();
            int ax = (int) getSetting("ArrayListX").getDoubleValue();
            int ay = (int) getSetting("ArrayListY").getDoubleValue();

            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, 1.0F);
            int drawX = (int)(ax / scale);
            int drawY = (int)(ay / scale);
            int y = drawY;

            // Expanded panel background if toggled
            if (expanded) {
                int size = (int) getSetting("ExpandedSize").getDoubleValue();
                int red = (int) getSetting("ExpandedRed").getDoubleValue();
                int green = (int) getSetting("ExpandedGreen").getDoubleValue();
                int blue = (int) getSetting("ExpandedBlue").getDoubleValue();
                int opacity = (int) getSetting("ExpandedOpacity").getDoubleValue();
                GuiScreen.drawRect(drawX - 5, drawY - 5, drawX + size, drawY + size, new Color(red, green, blue, opacity).getRGB());
                mc.fontRendererObj.drawStringWithShadow("HUD Settings", drawX, drawY - 10, Color.WHITE.getRGB());
            }

            // Render module names with color
            String colorMode = getSetting("ColorMode").getValue();
            for (int i = 0; i < enabledModules.size(); i++) {
                Color c = getColorForIndex(i, enabledModules.size(), colorMode);
                mc.fontRendererObj.drawStringWithShadow(enabledModules.get(i), drawX, y, c.getRGB());
                y += 10;
            }
            GlStateManager.popMatrix();

            // Click detection for expansion
            if (getSetting("ClickToExpand").getValue().equals("On") && Mouse.isButtonDown(0)) {
                int mouseX = Mouse.getX() * screenWidth / mc.displayWidth;
                int mouseY = screenHeight - Mouse.getY() * screenHeight / mc.displayHeight - 1;
                if (mouseX >= ax && mouseX <= ax + 120 * scale && mouseY >= ay && mouseY <= ay + enabledModules.size() * 10 * scale) {
                    expanded = !expanded;
                }
            }
        }
    }

    private Color getColorForIndex(int index, int total, String mode) {
        if (mode.equals("Single")) {
            return new Color((int)getSetting("SingleRed").getDoubleValue(),
                             (int)getSetting("SingleGreen").getDoubleValue(),
                             (int)getSetting("SingleBlue").getDoubleValue());
        } else if (mode.equals("Gradient") && total > 1) {
            float ratio = (float) index / (total - 1);
            int r1 = (int) getSetting("GradientStartRed").getDoubleValue();
            int g1 = (int) getSetting("GradientStartGreen").getDoubleValue();
            int b1 = (int) getSetting("GradientStartBlue").getDoubleValue();
            int r2 = (int) getSetting("GradientEndRed").getDoubleValue();
            int g2 = (int) getSetting("GradientEndGreen").getDoubleValue();
            int b2 = (int) getSetting("GradientEndBlue").getDoubleValue();
            int r = (int)(r1 + (r2 - r1) * ratio);
            int g = (int)(g1 + (g2 - g1) * ratio);
            int b = (int)(b1 + (b2 - b1) * ratio);
            return new Color(r, g, b);
        } else if (mode.equals("Rainbow")) {
            float speed = (float) getSetting("RainbowSpeed").getDoubleValue();
            float hue = (System.currentTimeMillis() % (long)(360 * speed)) / (360.0f * speed) + (float) index / total;
            return Color.getHSBColor(hue % 1.0f, 1.0f, 1.0f);
        }
        return Color.WHITE;
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
