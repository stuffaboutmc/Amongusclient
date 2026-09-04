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
import java.util.ArrayList;
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
    private int buttonWidth = 130;
    private int buttonHeight = 22;
    private int buttonX = 20;
    private int buttonStartY = 60;
    private int buttonSpacing = 32;
    private float backgroundTime = 0.0f;
    private List<Particle> particles = new ArrayList<>();
    private Random rand = new Random();

    public AugustusMainMenu() {
        try {
            AltManager.init();
            ThemeManager.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Initialize particles
        for (int i = 0; i < 100; i++) {
            particles.add(new Particle(rand.nextFloat() * width, rand.nextFloat() * height, rand.nextFloat() * 0.5f + 0.2f));
        }
    }

    private class Particle {
        float x, y, speed, size;
        Particle(float x, float y, float speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.size = rand.nextFloat() * 2.0f + 1.0f;
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
        backgroundTime += partialTicks * 0.05f;

        // Animated background: dark blue gradient with moving stars
        drawGradientBackground();
        drawParticles();

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        drawRect(0, 0, buttonWidth + 50, height, new Color(20, 20, 26, 230).getRGB());
        drawRect(buttonWidth + 50, 0, buttonWidth + 51, height, theme.accent.getRGB());

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.8, 1.8, 1.0);
        mc.fontRendererObj.drawStringWithShadow("Augustus", 20 / 1.8f, 20 / 1.8f, new Color(255, 255, 255, 255).getRGB());
        GlStateManager.popMatrix();

        String[] buttonNames = {"Singleplayer", "Multiplayer", "Alt Manager", "Settings", "Quit Game"};
        int buttonY = buttonStartY;
        for (String buttonName : buttonNames) {
            boolean hovered = mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight;
            if (hovered) {
                drawRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, new Color(45, 45, 55, 255).getRGB());
                drawRect(buttonX, buttonY, buttonX + 3, buttonY + buttonHeight, theme.accent.getRGB());
            } else {
                drawRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, new Color(30, 30, 38, 255).getRGB());
            }
            drawRect(buttonX, buttonY, buttonX + buttonWidth, buttonY + 1, new Color(60, 60, 70, 255).getRGB());
            drawRect(buttonX, buttonY + buttonHeight - 1, buttonX + buttonWidth, buttonY + buttonHeight, new Color(60, 60, 70, 255).getRGB());
            drawRect(buttonX, buttonY, buttonX + 1, buttonY + buttonHeight, new Color(60, 60, 70, 255).getRGB());
            drawRect(buttonX + buttonWidth - 1, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, new Color(60, 60, 70, 255).getRGB());
            mc.fontRendererObj.drawStringWithShadow(buttonName, buttonX + 12, buttonY + (buttonHeight - 8) / 2, theme.text.getRGB());
            buttonY += buttonSpacing;
        }

        String themeText = "Theme: " + theme.name;
        mc.fontRendererObj.drawStringWithShadow(themeText, buttonX + 5, height - 25, new Color(160, 160, 170, 255).getRGB());

        if (altManagerOpen) drawAltManager(mouseX, mouseY, theme);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawGradientBackground() {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        // Top deep blue
        wr.pos(0, 0, 0).color(10, 15, 40, 255).endVertex();
        wr.pos(width, 0, 0).color(10, 15, 40, 255).endVertex();
        wr.pos(width, height * 0.5f, 0).color(40, 20, 60, 255).endVertex();
        wr.pos(0, height * 0.5f, 0).color(40, 20, 60, 255).endVertex();
        // Bottom darker
        wr.pos(0, height * 0.5f, 0).color(40, 20, 60, 255).endVertex();
        wr.pos(width, height * 0.5f, 0).color(40, 20, 60, 255).endVertex();
        wr.pos(width, height, 0).color(15, 10, 25, 255).endVertex();
        wr.pos(0, height, 0).color(15, 10, 25, 255).endVertex();
        tess.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    private void drawParticles() {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        for (Particle p : particles) {
            p.y += p.speed;
            if (p.y > height) {
                p.y = 0;
                p.x = rand.nextFloat() * width;
            }
            float alpha = 0.6f;
            wr.pos(p.x, p.y, 0).color(255, 255, 255, (int)(alpha * 255)).endVertex();
            wr.pos(p.x + p.size, p.y, 0).color(255, 255, 255, (int)(alpha * 255)).endVertex();
            wr.pos(p.x + p.size, p.y + p.size, 0).color(255, 255, 255, (int)(alpha * 255)).endVertex();
            wr.pos(p.x, p.y + p.size, 0).color(255, 255, 255, (int)(alpha * 255)).endVertex();
        }
        tess.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    private void drawAltManager(int mx, int my, ThemeManager.Theme theme) {
        int px = buttonWidth + 80;
        int py = height / 2 - 120;
        int pw = 320;
        int ph = 240;
        drawRect(px, py, px + pw, py + ph, new Color(15, 15, 20, 250).getRGB());
        drawRect(px, py, px + pw, py + 1, theme.accent.getRGB());
        drawRect(px, py + ph - 1, px + pw, py + ph, theme.accent.getRGB());
        drawRect(px, py, px + 1, py + ph, theme.accent.getRGB());
        drawRect(px + pw - 1, py, px + pw, py + ph, theme.accent.getRGB());
        drawCenteredString(fontRendererObj, "Alt Manager", px + pw / 2, py + 10, theme.text.getRGB());

        List<AltManager.Alt> alts = AltManager.getAlts();
        for (int i = 0; i < Math.min(alts.size(), 8); i++) {
            int ay = py + 30 + i * 16;
            AltManager.Alt alt = alts.get(i);
            boolean hovered = mx >= px + 10 && mx <= px + pw - 10 && my >= ay && my <= ay + 13;
            if (hovered || i == selectedAlt) drawRect(px + 10, ay, px + pw - 10, ay + 13, new Color(50, 50, 65, 255).getRGB());
            drawString(fontRendererObj, alt.name, px + 15, ay + 3, alt.isTokenAlt ? new Color(255, 200, 50, 255).getRGB() : theme.text.getRGB());
        }

        int iy = py + ph - 100;
        drawRect(px + 10, iy, px + pw - 10, iy + 15, new Color(30, 30, 38, 255).getRGB());
        drawString(fontRendererObj, altInput.isEmpty() ? "Alt name..." : altInput, px + 15, iy + 4, theme.text.getRGB());
        drawRect(px + 10, iy + 20, px + pw - 10, iy + 35, new Color(30, 30, 38, 255).getRGB());
        drawString(fontRendererObj, emailInput.isEmpty() ? "Email..." : emailInput, px + 15, iy + 24, theme.text.getRGB());
        drawRect(px + 10, iy + 40, px + pw - 10, iy + 55, new Color(30, 30, 38, 255).getRGB());
        drawString(fontRendererObj, passwordInput.isEmpty() ? "Password..." : "********", px + 15, iy + 44, theme.text.getRGB());
        drawRect(px + 10, iy + 60, px + pw - 10, iy + 75, new Color(30, 30, 38, 255).getRGB());
        drawString(fontRendererObj, tokenInput.isEmpty() ? "Session ID / Token..." : tokenInput, px + 15, iy + 64, new Color(255, 200, 50, 255).getRGB());

        drawButton("Add Alt", px + 10, iy + 85, 80, 15, mx, my, theme);
        drawButton("Add Token", px + 100, iy + 85, 90, 15, mx, my, theme);
        drawButton("Login", px + 200, iy + 85, 70, 15, mx, my, theme);
        drawButton("Offline", px + 10, iy + 105, 70, 15, mx, my, theme);
        drawButton("Remove", px + 90, iy + 105, 70, 15, mx, my, theme);
        drawButton("Close", px + 170, iy + 105, 70, 15, mx, my, theme);
    }

    private void drawButton(String text, int x, int y, int w, int h, int mx, int my, ThemeManager.Theme theme) {
        boolean hovered = mx >= x && mx <= x + w && my >= y && my <= y + h;
        if (hovered) {
            drawRect(x, y, x + w, y + h, theme.header.getRGB());
            drawRect(x, y, x + 2, y + h, theme.accent.getRGB());
        } else {
            drawRect(x, y, x + w, y + h, theme.background.getRGB());
        }
        drawRect(x, y, x + w, y + 1, theme.outline.getRGB());
        drawRect(x, y + h - 1, x + w, y + h, theme.outline.getRGB());
        drawRect(x, y, x + 1, y + h, theme.outline.getRGB());
        drawRect(x + w - 1, y, x + w, y + h, theme.outline.getRGB());
        drawCenteredString(fontRendererObj, text, x + w / 2, y + (h - 8) / 2, theme.text.getRGB());
    }

    @Override
    protected void mouseClicked(int mx, int my, int mb) throws IOException {
        if (altManagerOpen) {
            int px = buttonWidth + 80;
            int py = height / 2 - 120;
            int pw = 320;
            int ph = 240;
            int iy = py + ph - 100;
            if (mb == 0 && mx >= px + 170 && mx <= px + 240 && my >= iy + 105 && my <= iy + 120) { altManagerOpen = false; focusedField = ""; return; }
            if (mb == 0 && mx >= px + 10 && mx <= px + 90 && my >= iy + 85 && my <= iy + 100) { if (!altInput.isEmpty()) { AltManager.addAlt(altInput, emailInput, passwordInput); altInput = ""; emailInput = ""; passwordInput = ""; } return; }
            if (mb == 0 && mx >= px + 100 && mx <= px + 190 && my >= iy + 85 && my <= iy + 100) { if (!altInput.isEmpty() && !tokenInput.isEmpty()) { AltManager.addTokenAlt(altInput, tokenInput); altInput = ""; tokenInput = ""; } return; }
            if (mb == 0 && mx >= px + 200 && mx <= px + 270 && my >= iy + 85 && my <= iy + 100) { if (selectedAlt >= 0 && selectedAlt < AltManager.getAlts().size()) AltManager.loginToAlt(AltManager.getAlts().get(selectedAlt)); return; }
            if (mb == 0 && mx >= px + 10 && mx <= px + 80 && my >= iy + 105 && my <= iy + 120) { if (!altInput.isEmpty()) AltManager.loginOffline(altInput); return; }
            if (mb == 0 && mx >= px + 90 && mx <= px + 160 && my >= iy + 105 && my <= iy + 120) { AltManager.removeAlt(selectedAlt); selectedAlt = -1; return; }
            if (mx >= px + 10 && mx <= px + pw - 10) {
                if (my >= iy && my <= iy + 15) focusedField = "name";
                if (my >= iy + 20 && my <= iy + 35) focusedField = "email";
                if (my >= iy + 40 && my <= iy + 55) focusedField = "password";
                if (my >= iy + 60 && my <= iy + 75) focusedField = "token";
            }
            List<AltManager.Alt> alts = AltManager.getAlts();
            for (int i = 0; i < Math.min(alts.size(), 8); i++) {
                int ay = py + 30 + i * 16;
                if (mx >= px + 10 && mx <= px + pw - 10 && my >= ay && my <= ay + 13) { selectedAlt = i; return; }
            }
            return;
        }

        String[] buttonNames = {"Singleplayer", "Multiplayer", "Alt Manager", "Settings", "Quit Game"};
        int buttonY = buttonStartY;
        for (int i = 0; i < buttonNames.length; i++) {
            if (mb == 0 && mx >= buttonX && mx <= buttonX + buttonWidth && my >= buttonY && my <= buttonY + buttonHeight) {
                switch (i) {
                    case 0: mc.displayGuiScreen(new GuiSelectWorld(this)); break;
                    case 1: mc.displayGuiScreen(new GuiMultiplayer(this)); break;
                    case 2: altManagerOpen = true; break;
                    case 3: mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings)); break;
                    case 4: mc.shutdown(); break;
                }
                return;
            }
            buttonY += buttonSpacing;
        }
        if (mb == 0 && mx >= buttonX && mx <= buttonX + 200 && my >= height - 25 && my <= height - 10) {
            List<ThemeManager.Theme> themes = ThemeManager.getThemes();
            int ci = -1;
            for (int i = 0; i < themes.size(); i++) if (themes.get(i) == ThemeManager.getCurrentTheme()) ci = i;
            ci++;
            if (ci >= themes.size()) ci = 0;
            ThemeManager.setTheme(themes.get(ci).name);
        }
    }

    @Override
    protected void keyTyped(char c, int kc) throws IOException {
        if (!altManagerOpen || focusedField.isEmpty()) { super.keyTyped(c, kc); return; }
        if (kc == Keyboard.KEY_BACK) {
            if (focusedField.equals("name")) altInput = altInput.substring(0, Math.max(0, altInput.length() - 1));
            if (focusedField.equals("email")) emailInput = emailInput.substring(0, Math.max(0, emailInput.length() - 1));
            if (focusedField.equals("password")) passwordInput = passwordInput.substring(0, Math.max(0, passwordInput.length() - 1));
            if (focusedField.equals("token")) tokenInput = tokenInput.substring(0, Math.max(0, tokenInput.length() - 1));
            return;
        }
        if (c >= 32 && c <= 126) {
            if (focusedField.equals("name")) altInput += c;
            if (focusedField.equals("email")) emailInput += c;
            if (focusedField.equals("password")) passwordInput += c;
            if (focusedField.equals("token")) tokenInput += c;
        }
    }

    @Override
    public void updateScreen() {}
}
