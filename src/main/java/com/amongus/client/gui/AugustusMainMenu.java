package com.amongus.client.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class AugustusMainMenu extends GuiScreen {
    private int selectedAlt = -1;
    private boolean altManagerOpen = false;
    private String altInput = "";
    private String emailInput = "";
    private String passwordInput = "";
    private String tokenInput = "";
    private String focusedField = "";
    private int buttonWidth = 120;
    private int buttonHeight = 20;
    private int buttonX = 20;
    private int buttonStartY = 50;
    private int buttonSpacing = 28;
    private float backgroundTime = 0.0f;

    public AugustusMainMenu() {
        try {
            AltManager.init();
            ThemeManager.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        backgroundTime += partialTicks * 0.001f; // slow drift

        drawAnimatedBackground(partialTicks);

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        drawRect(0, 0, buttonWidth + 40, height, new Color(22, 22, 28, 240).getRGB());
        drawRect(buttonWidth + 40, 0, buttonWidth + 41, height, new Color(255, 0, 0, 200).getRGB());

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.6, 1.6, 1.0);
        mc.fontRendererObj.drawStringWithShadow("Augustus", 15 / 1.6f, 15 / 1.6f, new Color(255, 255, 255, 255).getRGB());
        GlStateManager.popMatrix();

        String[] buttonNames = {"Singleplayer", "Multiplayer", "Alt Manager", "Settings", "Quit Game"};
        int buttonY = buttonStartY;
        for (String buttonName : buttonNames) {
            boolean hovered = mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight;
            if (hovered) {
                drawRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, new Color(35, 35, 42, 255).getRGB());
                drawRect(buttonX, buttonY, buttonX + 2, buttonY + buttonHeight, theme.accent.getRGB());
            } else {
                drawRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, new Color(28, 28, 34, 255).getRGB());
            }
            drawRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + 1, new Color(50, 50, 58, 255).getRGB());
            drawRect(buttonX, buttonY + buttonHeight - 1, buttonX + buttonWidth, buttonY + buttonHeight, new Color(50, 50, 58, 255).getRGB());
            drawRect(buttonX, buttonY, buttonX + 1, buttonY + buttonHeight, new Color(50, 50, 58, 255).getRGB());
            drawRect(buttonX + buttonWidth - 1, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, new Color(50, 50, 58, 255).getRGB());
            mc.fontRendererObj.drawStringWithShadow(buttonName, buttonX + 10, buttonY + (buttonHeight - 8) / 2, theme.text.getRGB());
            buttonY += buttonSpacing;
        }

        String themeText = "Theme: " + theme.name;
        mc.fontRendererObj.drawStringWithShadow(themeText, buttonX + 5, height - 20, new Color(160, 160, 165, 255).getRGB());

        if (altManagerOpen) drawAltManager(mouseX, mouseY, theme);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawAnimatedBackground(float partialTicks) {
        // Sky gradient (distant horizon effect)
        float horizonY = height * 0.65f;
        // Top color: deep blue
        Color top = new Color(20, 30, 60, 255);
        // Horizon color: warm orange/pink
        Color horizon = new Color(255, 150, 80, 255);
        // Bottom ground color: dark
        Color ground = new Color(30, 20, 15, 255);

        // Draw vertical gradient sky using quads
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < 100; i++) {
            float y1 = (i / 100.0f) * horizonY;
            float y2 = ((i + 1) / 100.0f) * horizonY;
            float t = i / 100.0f;
            int r = (int) (top.getRed() + (horizon.getRed() - top.getRed()) * t);
            int g = (int) (top.getGreen() + (horizon.getGreen() - top.getGreen()) * t);
            int b = (int) (top.getBlue() + (horizon.getBlue() - top.getBlue()) * t);
            wr.pos(0, y2, 0).color(r, g, b, 255).endVertex();
            wr.pos(width, y2, 0).color(r, g, b, 255).endVertex();
            wr.pos(width, y1, 0).color(r, g, b, 255).endVertex();
            wr.pos(0, y1, 0).color(r, g, b, 255).endVertex();
        }
        tess.draw();

        // Ground
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(0, horizonY, 0).color(ground.getRed(), ground.getGreen(), ground.getBlue(), 255).endVertex();
        wr.pos(width, horizonY, 0).color(ground.getRed(), ground.getGreen(), ground.getBlue(), 255).endVertex();
        wr.pos(width, height, 0).color(ground.getRed(), ground.getGreen(), ground.getBlue(), 255).endVertex();
        wr.pos(0, height, 0).color(ground.getRed(), ground.getGreen(), ground.getBlue(), 255).endVertex();
        tess.draw();

        // Moving clouds (simple white ellipses)
        GlStateManager.enableTexture2D();
        for (int i = 0; i < 5; i++) {
            float x = (backgroundTime * 10 + i * 150) % (width + 200) - 100;
            float y = 50 + i * 30;
            drawCloud(x, y, 80 + i * 20, 20 + i * 5);
        }

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    private void drawCloud(float cx, float cy, float rx, float ry) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(cx, cy, 0).color(255, 255, 255, 180).endVertex();
        for (int i = 0; i <= 360; i += 10) {
            double angle = Math.toRadians(i);
            float px = cx + (float) (Math.cos(angle) * rx);
            float py = cy + (float) (Math.sin(angle) * ry);
            wr.pos(px, py, 0).color(255, 255, 255, 180).endVertex();
        }
        tess.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private void drawAltManager(int mx, int my, ThemeManager.Theme theme) {
        int px = buttonWidth + 80;
        int py = height / 2 - 100;
        int pw = 300;
        int ph = 200;
        drawRect(px, py, px + pw, py + ph, new Color(20, 20, 25, 240).getRGB());
        drawRect(px, py, px + pw, py + 1, theme.accent.getRGB());
        drawRect(px, py + ph - 1, px + pw, py + ph, theme.accent.getRGB());
        drawRect(px, py, px + 1, py + ph, theme.accent.getRGB());
        drawRect(px + pw - 1, py, px + pw, py + ph, theme.accent.getRGB());
        drawCenteredString(fontRendererObj, "Alt Manager", px + pw / 2, py +
