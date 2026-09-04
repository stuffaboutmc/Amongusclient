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
import java.util.Date;
import java.util.List;

public class HUD extends Module {
    private static final ResourceLocation IMPOSTER = new ResourceLocation("amongus", "textures/imposter.png");

    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.RENDER, "Top-right info panel with modules and styled watermark.");
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
        addSetting(new Setting("Coordinates", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("FPS", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Ping", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Time", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("Direction", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("ModuleList", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("ModuleListBG", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("BGRed", 0, 255, 10, 1));
        addSetting(new Setting("BGGreen", 0, 255, 10, 1));
        addSetting(new Setting("BGBlue", 0, 255, 10, 1));
        addSetting(new Setting("BGOpacity", 0, 255, 180, 5));
        addSetting(new Setting("TextRed", 0, 255, 230, 1));
        addSetting(new Setting("TextGreen", 0, 255, 230, 1));
        addSetting(new Setting("TextBlue", 0, 255, 235, 1));
        addSetting(new Setting("SortAlphabetical", new String[]{"Off","On"}, "On"));
        addSetting(new Setting("WatermarkX", 1, 400, 5, 1));
        addSetting(new Setting("WatermarkY", 1, 200, 5, 1));
        addSetting(new Setting("InfoX", 1, 400, 5, 1));
        addSetting(new Setting("InfoY", 1, 200, 5, 1));
    }

    @Override
    public void onEnable() {
        // Set default top-right position if using sliders? We'll rely on sliders with defaults adjusted.
        // But sliders have fixed defaults 5,5 etc. We'll override via settings in constructor? Not possible with current Setting.
        // Instead, we'll position based on screen width in the render method if user hasn't moved sliders.
        // We'll add a flag: if InfoX/InfoY are at default (5,5) then use top-right.
        // Let's set InfoX and InfoY defaults to -1 to indicate auto? But sliders min 1, so can't be -1.
        // We'll just position using screen width minus some offset as base, and allow sliders to offset further.
        // For simplicity, we'll compute baseX = screenWidth - 200, baseY = 5, then add slider offsets.
        // Watermark X/Y similar.
        super.onEnable();
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (mc.theWorld == null || mc.thePlayer == null) return;

        int screenWidth = event.resolution.getScaledWidth();
        int screenHeight = event.resolution.getScaledHeight();

        // Top-right base position
        int watermarkBaseX = screenWidth - 200;
        int watermarkBaseY = 5;
        int infoBaseX = screenWidth - 200;
        int infoBaseY = 30;

        // Apply user offsets (sliders act as additional offset)
        int wx = watermarkBaseX + (int) getSetting("WatermarkX").getDoubleValue() - 5; // -5 to neutralize default
        int wy = watermarkBaseY + (int) getSetting("WatermarkY").getDoubleValue() - 5;
        int ix = infoBaseX + (int) getSetting("InfoX").getDoubleValue() - 5;
        int iy = infoBaseY + (int) getSetting("InfoY").getDoubleValue() - 5;

        int imageSize = (int) getSetting("ImageSize").getDoubleValue();

        // Watermark
        String watermarkMode = getSetting("Watermark").getValue();
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

            if (getSetting("WatermarkShadow").getValue().equals("On")) {
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY + 1, new Color(0,0,0,180).getRGB());
            }
            if (getSetting("WatermarkGlow").getValue().equals("On")) {
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX, scaledY - 1, new Color(red, green, blue, 60).getRGB());
                mc.fontRendererObj.drawString(name, scaledX, scaledY + 1, new Color(red, green, blue, 60).getRGB());
            }
            mc.fontRendererObj.drawString(name, scaledX, scaledY, color.getRGB());
            if (getSetting("WatermarkOutline").getValue().equals("On")) {
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY - 1, new Color(0,0,0,255).getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY - 1, new Color(0,0,0,255).getRGB());
                mc.fontRendererObj.drawString(name, scaledX - 1, scaledY + 1, new Color(0,0,0,255).getRGB());
                mc.fontRendererObj.drawString(name, scaledX + 1, scaledY + 1, new Color(0,0,0,255).getRGB());
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
        int currentY = iy;
        int lineHeight = 10;
        int infoX = ix;

        if (getSetting("Coordinates").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow("XYZ: " + (int)mc.thePlayer.posX + " " + (int)mc.thePlayer.posY + " " + (int)mc.thePlayer.posZ, infoX, currentY, -1);
            currentY += lineHeight;
        }
        if (getSetting("FPS").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow("FPS: " + mc.debug.split(" ")[0], infoX, currentY, -1);
            currentY += lineHeight;
        }
        if (getSetting("Ping").getValue().equals("On")) {
            int ping = 0;
            if (mc.getNetHandler() != null && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null) {
                ping = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
            }
            mc.fontRendererObj.drawStringWithShadow("Ping: " + ping + "ms", infoX, currentY, -1);
            currentY += lineHeight;
        }
        if (getSetting("Time").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow(new SimpleDateFormat("HH:mm:ss").format(new Date()), infoX, currentY, -1);
            currentY += lineHeight;
        }
        if (getSetting("Direction").getValue().equals("On")) {
            mc.fontRendererObj.drawStringWithShadow(getDirection(), infoX, currentY, -1);
        }

        // Module list below info, also top-right aligned
        if (getSetting("ModuleList").getValue().equals("On")) {
            List<String> enabledModules = new ArrayList<>();
            for (Module m : AmongusClient.moduleManager.getModules()) {
                if (m.isEnabled()) enabledModules.add(m.getName());
            }
            if (getSetting("SortAlphabetical").getValue().equals("On")) {
                Collections.sort(enabledModules);
            }

            int mlx = infoX;
            int mly = currentY + 2;
            int textRed = (int) getSetting("TextRed").getDoubleValue();
            int textGreen = (int) getSetting("TextGreen").getDoubleValue();
            int textBlue = (int) getSetting("TextBlue").getDoubleValue();
            Color textColor = new Color(textRed, textGreen, textBlue, 255);

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

            int yPos = mly;
            for (String name : enabledModules) {
                mc.fontRendererObj.drawStringWithShadow(name, mlx, yPos, textColor.getRGB());
                yPos += 10;
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
