package com.amongus.client.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class AugustusMainMenu extends GuiScreen {
    private int selectedAlt = -1;
    private boolean altManagerOpen = false;
    private String altInput = "";
    private String emailInput = "";
    private String passwordInput = "";
    private String tokenInput = "";
    private String focusedField = "";

    public AugustusMainMenu() {
        AltManager.init();
        ThemeManager.init();
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
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        drawRect(0, 0, width, height, new Color(15, 15, 20, 255).getRGB());
        for (int i = 0; i < height; i++) {
            int alpha = (int) (40 * (1.0 - (double) i / height));
            drawRect(0, i, width, i + 1, new Color(255, 0, 0, alpha).getRGB());
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(3.0, 3.0, 1.0);
        drawCenteredString(fontRendererObj, "Amongus", width / 6, height / 12, new Color(255, 0, 0, 255).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.5, 1.5, 1.0);
        drawCenteredString(fontRendererObj, "Client v1.0", width / 3, height / 6 + 30, new Color(200, 200, 200, 255).getRGB());
        GlStateManager.popMatrix();
        drawAugustusButton("Singleplayer", width / 2 - 100, height / 2 - 40, 200, 20, mouseX, mouseY, theme);
        drawAugustusButton("Multiplayer", width / 2 - 100, height / 2 - 15, 200, 20, mouseX, mouseY, theme);
        drawAugustusButton("Alt Manager", width / 2 - 100, height / 2 + 10, 200, 20, mouseX, mouseY, theme);
        drawAugustusButton("Settings", width / 2 - 100, height / 2 + 35, 200, 20, mouseX, mouseY, theme);
        drawAugustusButton("Quit Game", width / 2 - 100, height / 2 + 60, 200, 20, mouseX, mouseY, theme);
        drawCenteredString(fontRendererObj, "Theme: " + theme.name + " (click to cycle)", width / 2, height - 30, new Color(180, 180, 180, 255).getRGB());
        if (altManagerOpen) drawAltManager(mouseX, mouseY, theme);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawAugustusButton(String text, int x, int y, int w, int h, int mx, int my, ThemeManager.Theme theme) {
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

    private void drawAltManager(int mx, int my, ThemeManager.Theme theme) {
        int px = width / 2 - 150;
        int py = height / 2 - 100;
        int pw = 300;
        int ph = 200;
        drawRect(px, py, px + pw, py + ph, new Color(20, 20, 25, 240).getRGB());
        drawRect(px, py, px + pw, py + 1, theme.accent.getRGB());
        drawRect(px, py + ph - 1, px + pw, py + ph, theme.accent.getRGB());
        drawRect(px, py, px + 1, py + ph, theme.accent.getRGB());
        drawRect(px + pw - 1, py, px + pw, py + ph, theme.accent.getRGB());
        drawCenteredString(fontRendererObj, "Alt Manager", px + pw / 2, py + 10, theme.text.getRGB());
        List<AltManager.Alt> alts = AltManager.getAlts();
        for (int i = 0; i < Math.min(alts.size(), 8); i++) {
            int ay = py + 30 + i * 15;
            AltManager.Alt alt = alts.get(i);
            boolean hovered = mx >= px + 10 && mx <= px + pw - 10 && my >= ay && my <= ay + 12;
            if (hovered || i == selectedAlt) drawRect(px + 10, ay, px + pw - 10, ay + 12, new Color(50, 50, 60, 255).getRGB());
            drawString(fontRendererObj, alt.name, px + 15, ay + 3, alt.isTokenAlt ? new Color(255, 200, 50, 255).getRGB() : theme.text.getRGB());
        }
        int iy = py + ph - 80;
        drawRect(px + 10, iy, px + pw - 10, iy + 15, new Color(30, 30, 35, 255).getRGB());
        drawString(fontRendererObj, altInput.isEmpty() ? "Alt name..." : altInput, px + 15, iy + 4, theme.text.getRGB());
        drawRect(px + 10, iy + 20, px + pw - 10, iy + 35, new Color(30, 30, 35, 255).getRGB());
        drawString(fontRendererObj, emailInput.isEmpty() ? "Email..." : emailInput, px + 15, iy + 24, theme.text.getRGB());
        drawRect(px + 10, iy + 40, px + pw - 10, iy + 55, new Color(30, 30, 35, 255).getRGB());
        drawString(fontRendererObj, passwordInput.isEmpty() ? "Password..." : "********", px + 15, iy + 44, theme.text.getRGB());
        drawRect(px + 10, iy + 60, px + pw - 10, iy + 75, new Color(30, 30, 35, 255).getRGB());
        drawString(fontRendererObj, tokenInput.isEmpty() ? "Session ID / Token..." : tokenInput, px + 15, iy + 64, new Color(255, 200, 50, 255).getRGB());
        drawAugustusButton("Add Alt", px + 10, iy + 85, 80, 15, mx, my, theme);
        drawAugustusButton("Add Token", px + 100, iy + 85, 80, 15, mx, my, theme);
        drawAugustusButton("Login", px + 190, iy + 85, 80, 15, mx, my, theme);
        drawAugustusButton("Offline", px + 10, iy + 105, 80, 15, mx, my, theme);
        drawAugustusButton("Remove", px + 100, iy + 105, 80, 15, mx, my, theme);
        drawAugustusButton("Close", px + 190, iy + 105, 80, 15, mx, my, theme);
    }

    @Override
    protected void mouseClicked(int mx, int my, int mb) throws IOException {
        if (altManagerOpen) {
            int px = width / 2 - 150;
            int py = height / 2 - 100;
            int pw = 300;
            int ph = 200;
            int iy = py + ph - 80;
            if (mb == 0 && mx >= px + 190 && mx <= px + 270 && my >= iy + 105 && my <= iy + 120) { altManagerOpen = false; focusedField = ""; return; }
            if (mb == 0 && mx >= px + 10 && mx <= px + 90 && my >= iy + 85 && my <= iy + 100) { if (!altInput.isEmpty()) { AltManager.addAlt(altInput, emailInput, passwordInput); altInput = ""; emailInput = ""; passwordInput = ""; } return; }
            if (mb == 0 && mx >= px + 100 && mx <= px + 180 && my >= iy + 85 && my <= iy + 100) { if (!altInput.isEmpty() && !tokenInput.isEmpty()) { AltManager.addTokenAlt(altInput, tokenInput); altInput = ""; tokenInput = ""; } return; }
            if (mb == 0 && mx >= px + 190 && mx <= px + 270 && my >= iy + 85 && my <= iy + 100) { if (selectedAlt >= 0 && selectedAlt < AltManager.getAlts().size()) AltManager.loginToAlt(AltManager.getAlts().get(selectedAlt)); return; }
            if (mb == 0 && mx >= px + 10 && mx <= px + 90 && my >= iy + 105 && my <= iy + 120) { if (!altInput.isEmpty()) AltManager.loginOffline(altInput); return; }
            if (mb == 0 && mx >= px + 100 && mx <= px + 180 && my >= iy + 105 && my <= iy + 120) { AltManager.removeAlt(selectedAlt); selectedAlt = -1; return; }
            if (mx >= px + 10 && mx <= px + pw - 10) {
                if (my >= iy && my <= iy + 15) focusedField = "name";
                if (my >= iy + 20 && my <= iy + 35) focusedField = "email";
                if (my >= iy + 40 && my <= iy + 55) focusedField = "password";
                if (my >= iy + 60 && my <= iy + 75) focusedField = "token";
            }
            List<AltManager.Alt> alts = AltManager.getAlts();
            for (int i = 0; i < Math.min(alts.size(), 8); i++) {
                int ay = py + 30 + i * 15;
                if (mx >= px + 10 && mx <= px + pw - 10 && my >= ay && my <= ay + 12) { selectedAlt = i; return; }
            }
            return;
        }
        if (mb == 0 && mx >= width / 2 - 100 && mx <= width / 2 + 100) {
            if (my >= height / 2 - 40 && my <= height / 2 - 20) mc.displayGuiScreen(new GuiSelectWorld(this));
            else if (my >= height / 2 - 15 && my <= height / 2 + 5) mc.displayGuiScreen(new GuiMultiplayer(this));
            else if (my >= height / 2 + 10 && my <= height / 2 + 30) altManagerOpen = true;
            else if (my >= height / 2 + 35 && my <= height / 2 + 55) mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
            else if (my >= height / 2 + 60 && my <= height / 2 + 80) mc.shutdown();
        }
        if (mb == 0 && mx >= width / 2 - 50 && mx <= width / 2 + 50 && my >= height - 35 && my <= height - 20) {
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
