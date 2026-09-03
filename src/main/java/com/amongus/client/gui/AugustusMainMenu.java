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
    private int buttonWidth = 120;
    private int buttonHeight = 20;
    private int buttonX = 20;
    private int buttonStartY = 50;
    private int buttonSpacing = 28;

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
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        drawRect(0, 0, width, height, new Color(15, 15, 20, 255).getRGB());
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

        drawButton("Add Alt", px + 10, iy + 85, 80, 15, mx, my, theme);
        drawButton("Add Token", px + 100, iy + 85, 80, 15, mx, my, theme);
        drawButton("Login", px + 190, iy + 85, 80, 15, mx, my, theme);
        drawButton("Offline", px + 10, iy + 105, 80, 15, mx, my, theme);
        drawButton("Remove", px + 100, iy + 105, 80, 15, mx, my, theme);
        drawButton("Close", px + 190, iy + 105, 80, 15, mx, my, theme);
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
