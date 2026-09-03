package com.amongus.client.modules.render;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Module {
    private static final Color WINDOW_BG = new Color(42, 42, 47, 255);
    private static final Color TAB_ACTIVE = new Color(65, 65, 73, 255);
    private static final Color TAB_INACTIVE = new Color(32, 32, 37, 255);
    private static final Color MODULE_ENABLED = new Color(0, 255, 128, 255);
    private static final Color MODULE_DISABLED = new Color(165, 165, 170, 255);
    private static final Color SETTING_LABEL = new Color(135, 135, 142, 255);
    private static final Color SETTING_VALUE = new Color(255, 255, 255, 255);
    private static final Color SETTING_VALUE_SLIDER = new Color(255, 200, 50, 255);
    private static final Color OUTLINE = new Color(65, 65, 72, 255);
    private static final Color TITLE_TEXT = new Color(255, 255, 255, 255);
    private static final Color HOVER_BG = new Color(55, 55, 62, 255);
    private static final Color ACCENT_GREEN = new Color(0, 255, 128, 255);
    private static final Color CLOSE_RED = new Color(210, 55, 55, 255);
    private static final Color MINIMIZE_YELLOW = new Color(210, 180, 55, 255);
    private static final Color MAXIMIZE_GREEN = new Color(55, 185, 85, 255);
    private static final Color SAVE_BLUE = new Color(65, 125, 225, 255);
    private static final Color LOAD_PURPLE = new Color(105, 85, 185, 255);
    private static final Color SCROLLBAR_TRACK = new Color(25, 25, 29, 255);
    private static final Color SCROLLBAR_THUMB = new Color(80, 80, 88, 255);

    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER, "Opens the panel menu.");
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new AugustusGuiScreen());
    }

    public class AugustusGuiScreen extends GuiScreen {
        private String[] categoryNames = {"Combat", "Movement", "Render", "Player", "Misc"};
        private int activeTab = 0;
        private Module selectedModule = null;
        private float openAnimation = 0.0F;
        private int scrollOffset = 0;
        private int maxScroll = 0;
        private int windowWidth = 480;
        private int windowHeight = 320;
        private boolean minimized = false;
        private boolean maximized = false;
        private boolean dragging = false;
        private int dragOffsetX, dragOffsetY;
        private int windowX, windowY;

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            if (openAnimation < 1.0F) openAnimation += 0.08F;
            if (openAnimation > 1.0F) openAnimation = 1.0F;
            int w = maximized ? width : (int)(windowWidth * openAnimation);
            int h = maximized ? height : (int)(windowHeight * openAnimation);
            windowX = maximized ? 0 : (width - w) / 2;
            windowY = maximized ? 0 : (height - h) / 2;
            if (dragging) { windowX = mouseX - dragOffsetX; windowY = mouseY - dragOffsetY; }

            if (minimized) {
                drawRect(0, 0, 120, 20, new Color(35, 35, 40, 255).getRGB());
                drawRect(0, 0, 3, 20, ACCENT_GREEN.getRGB());
                mc.fontRendererObj.drawStringWithShadow("Augustus", 8, 6, TITLE_TEXT.getRGB());
                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }

            drawRect(windowX + 3, windowY + 3, windowX + w + 3, windowY + h + 3, new Color(0, 0, 0, 80).getRGB());
            drawRect(windowX, windowY, windowX + w, windowY + h, WINDOW_BG.getRGB());
            drawRect(windowX, windowY, windowX + w, windowY + 1, OUTLINE.getRGB());
            drawRect(windowX, windowY + h - 1, windowX + w, windowY + h, OUTLINE.getRGB());
            drawRect(windowX, windowY, windowX + 1, windowY + h, OUTLINE.getRGB());
            drawRect(windowX + w - 1, windowY, windowX + w, windowY + h, OUTLINE.getRGB());

            for (int i = 0; i < 26; i++) {
                drawRect(windowX, windowY + i, windowX + w, windowY + i + 1, new Color(30, 30, 35, 255).getRGB());
            }
            drawRect(windowX, windowY, windowX + 3, windowY + 26, ACCENT_GREEN.getRGB());

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.35, 1.35, 1.0);
            mc.fontRendererObj.drawStringWithShadow("Augustus", (windowX + 10) / 1.35f, (windowY + 7) / 1.35f, TITLE_TEXT.getRGB());
            GlStateManager.popMatrix();

            int cx = windowX + w - 75;
            int cy = windowY + 6;
            drawRect(cx, cy, cx + 16, cy + 14, MINIMIZE_YELLOW.getRGB()); mc.fontRendererObj.drawString("_", cx + 5, cy + 3, new Color(255, 255, 255, 255).getRGB());
            drawRect(cx + 20, cy, cx + 36, cy + 14, MAXIMIZE_GREEN.getRGB()); mc.fontRendererObj.drawString("□", cx + 25, cy + 3, new Color(255, 255, 255, 255).getRGB());
            drawRect(cx + 40, cy, cx + 56, cy + 14, CLOSE_RED.getRGB()); mc.fontRendererObj.drawString("X", cx + 45, cy + 3, new Color(255, 255, 255, 255).getRGB());
            drawRect(cx - 30, cy, cx - 10, cy + 14, SAVE_BLUE.getRGB()); mc.fontRendererObj.drawString("S", cx - 26, cy + 3, new Color(255, 255, 255, 255).getRGB());
            drawRect(cx - 56, cy, cx - 36, cy + 14, LOAD_PURPLE.getRGB()); mc.fontRendererObj.drawString("L", cx - 52, cy + 3, new Color(255, 255, 255, 255).getRGB());

            int tx = windowX + 12;
            int ty = windowY + 30;
            for (int i = 0; i < categoryNames.length; i++) {
                int tw = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 18;
                boolean active = i == activeTab;
                drawRect(tx, ty, tx + tw, ty + 16, active ? TAB_ACTIVE.getRGB() : TAB_INACTIVE.getRGB());
                drawRect(tx, ty, tx + tw, ty + 1, OUTLINE.getRGB());
                if (active) drawRect(tx, ty + 14, tx + tw, ty + 16, ACCENT_GREEN.getRGB());
                mc.fontRendererObj.drawString(categoryNames[i], tx + 9, ty + 5, active ? new Color(255, 255, 255, 255).getRGB() : new Color(145, 145, 150, 255).getRGB());
                tx += tw + 3;
            }

            int dWheel = Mouse.getDWheel();
            if (dWheel > 0) scrollOffset = Math.max(0, scrollOffset - 1);
            if (dWheel < 0) scrollOffset = Math.min(maxScroll, scrollOffset + 1);

            List<Module> modules = getModules(activeTab);
            maxScroll = Math.max(0, modules.size() - 18);
            int mx = windowX + 12;
            int my = windowY + 52 - (scrollOffset * 13);

            for (Module m : modules) {
                if (my < windowY + 50) { my += 13; continue; }
                if (my > windowY + h - 18) break;
                boolean hovered = mouseX >= mx && mouseX <= mx + 110 && mouseY >= my && mouseY <= my + 12;
                boolean selected = m == selectedModule;
                if (hovered || selected) {
                    drawRect(mx - 4, my - 1, mx + 110, my + 12, HOVER_BG.getRGB());
                    if (selected) drawRect(mx - 4, my - 1, mx - 2, my + 12, ACCENT_GREEN.getRGB());
                }
                mc.fontRendererObj.drawStringWithShadow(m.getName(), mx, my + 2, m.isEnabled() ? MODULE_ENABLED.getRGB() : MODULE_DISABLED.getRGB());
                if (selected && m.getSettings().size() > 0) {
                    int sx = mx + 120;
                    int sy = my - (m.getSettings().size() / 2) * 12;
                    for (Module.Setting setting : m.getSettings()) {
                        if (sy < windowY + 50) { sy += 12; continue; }
                        if (sy > windowY + h - 18) break;
                        String sn = setting.getName();
                        String sv = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();
                        mc.fontRendererObj.drawString(sn + ":", sx, sy + 2, SETTING_LABEL.getRGB());
                        int vx = sx + mc.fontRendererObj.getStringWidth(sn) + 12;
                        drawRect(vx - 2, sy, vx + mc.fontRendererObj.getStringWidth(sv) + 4, sy + 11, new Color(30, 30, 35, 255).getRGB());
                        mc.fontRendererObj.drawString(sv, vx, sy + 2, setting.isSlider() ? SETTING_VALUE_SLIDER.getRGB() : SETTING_VALUE.getRGB());
                        sy += 13;
                    }
                }
                my += 13;
            }

            if (maxScroll > 0) {
                int sbX = windowX + w - 6;
                int sbH = h - 60;
                drawRect(sbX, windowY + 52, sbX + 4, windowY + 52 + sbH, SCROLLBAR_TRACK.getRGB());
                int thumbH = Math.max(20, sbH / (maxScroll + 1));
                int thumbY = windowY + 52 + (int)((double)scrollOffset / maxScroll * (sbH - thumbH));
                drawRect(sbX, thumbY, sbX + 4, thumbY + thumbH, SCROLLBAR_THUMB.getRGB());
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        private List<Module> getModules(int tab) {
            List<Module> result = new ArrayList<>();
            Module.Category cat = Module.Category.values()[tab];
            for (Module m : AmongusClient.moduleManager.getModules()) if (m.getCategory() == cat) result.add(m);
            return result;
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (minimized) {
                if (mouseX >= 0 && mouseX <= 120 && mouseY >= 0 && mouseY <= 20) minimized = false;
                return;
            }
            int w = maximized ? width : windowWidth;
            int h = maximized ? height : windowHeight;
            int cx = windowX + w - 75;
            int cy = windowY + 6;
            if (mouseX >= cx && mouseX <= cx + 16 && mouseY >= cy && mouseY <= cy + 14) { minimized = true; return; }
            if (mouseX >= cx + 20 && mouseX <= cx + 36 && mouseY >= cy && mouseY <= cy + 14) { maximized = !maximized; return; }
            if (mouseX >= cx + 40 && mouseX <= cx + 56 && mouseY >= cy && mouseY <= cy + 14) { mc.displayGuiScreen(null); return; }
            if (mouseY >= windowY && mouseY <= windowY + 26 && mouseX >= windowX && mouseX <= windowX + w - 75) { dragging = true; dragOffsetX = mouseX - windowX; dragOffsetY = mouseY - windowY; return; }

            int tx = windowX + 12;
            int ty = windowY + 30;
            for (int i = 0; i < categoryNames.length; i++) {
                int tw = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 18;
                if (mouseX >= tx && mouseX <= tx + tw && mouseY >= ty && mouseY <= ty + 16) { activeTab = i; selectedModule = null; return; }
                tx += tw + 3;
            }

            List<Module> modules = getModules(activeTab);
            int mx = windowX + 12;
            int my = windowY + 52 - (scrollOffset * 13);
            for (Module m : modules) {
                if (my < windowY + 50) { my += 13; continue; }
                if (my > windowY + h - 18) break;
                if (mouseX >= mx && mouseX <= mx + 110 && mouseY >= my && mouseY <= my + 12) {
                    if (mouseButton == 0) { m.toggle(); return; }
                    if (mouseButton == 1) { selectedModule = (selectedModule == m) ? null : m; return; }
                }
                my += 13;
            }

            if (selectedModule != null) {
                int idx = modules.indexOf(selectedModule);
                if (idx >= 0) {
                    int sx = mx + 120;
                    int sy = windowY + 52 - (scrollOffset * 13) + (idx * 13) - (selectedModule.getSettings().size() / 2) * 12;
                    for (Module.Setting setting : selectedModule.getSettings()) {
                        int vx = sx + mc.fontRendererObj.getStringWidth(setting.getName()) + 12;
                        String sv = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();
                        int vw = mc.fontRendererObj.getStringWidth(sv);
                        if (mouseX >= vx - 2 && mouseX <= vx + vw + 4 && mouseY >= sy && mouseY <= sy + 11) {
                            if (!setting.isSlider()) { setting.cycle(); }
                            else {
                                double cur = setting.getDoubleValue();
                                if (mouseButton == 0) { double next = cur + setting.getIncrement(); if (next <= setting.getMax()) setting.setValue(String.valueOf(next)); }
                                if (mouseButton == 1) { double next = cur - setting.getIncrement(); if (next >= setting.getMin()) setting.setValue(String.valueOf(next)); }
                            }
                            return;
                        }
                        sy += 13;
                    }
                }
            }
        }

        @Override
        protected void mouseReleased(int mouseX, int mouseY, int state) { dragging = false; }
        @Override
        public boolean doesGuiPauseGame() { return false; }
    }
}
