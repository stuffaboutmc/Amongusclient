package com.amongus.client.modules.render;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.ConfigManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Module {
    private static final Color WINDOW_BG = new Color(40, 40, 45, 255);
    private static final Color TITLE_BAR = new Color(28, 28, 32, 255);
    private static final Color TAB_ACTIVE = new Color(58, 58, 65, 255);
    private static final Color TAB_INACTIVE = new Color(28, 28, 31, 255);
    private static final Color TAB_HOVER = new Color(44, 44, 49, 255);
    private static final Color MODULE_ENABLED = new Color(230, 230, 235, 255);
    private static final Color MODULE_DISABLED = new Color(130, 130, 135, 255);
    private static final Color MODULE_TOGGLED_BG = new Color(18, 18, 20, 255);
    private static final Color SETTING_LABEL = new Color(120, 120, 125, 255);
    private static final Color SETTING_VALUE = new Color(240, 240, 245, 255);
    private static final Color SETTING_VALUE_SLIDER = new Color(255, 200, 50, 255);
    private static final Color OUTLINE = new Color(55, 55, 60, 255);
    private static final Color TITLE_TEXT = new Color(255, 255, 255, 255);
    private static final Color HOVER_BG = new Color(50, 50, 55, 255);
    private static final Color ACCENT_WHITE = new Color(200, 200, 205, 255);
    private static final Color CONTROL_BUTTON = new Color(70, 70, 75, 255);
    private static final Color CONTROL_BUTTON_HOVER = new Color(90, 90, 96, 255);
    private static final Color SCROLLBAR_TRACK = new Color(22, 22, 24, 255);
    private static final Color SCROLLBAR_THUMB = new Color(70, 70, 76, 255);
    private static final Color TOOLTIP_BG = new Color(8, 8, 10, 240);
    private static final Color TOOLTIP_BORDER = new Color(60, 60, 66, 255);
    private static final Color TOOLTIP_TEXT = new Color(220, 220, 225, 255);

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
        private int windowWidth = 500;
        private int windowHeight = 340;
        private boolean minimized = false;
        private boolean maximized = false;
        private boolean dragging = false;
        private int dragOffsetX, dragOffsetY;
        private int windowX, windowY;
        private Module hoveredModule = null;
        private boolean windowVisible = true;

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawIcons(mouseX, mouseY);

            if (!windowVisible) {
                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }

            if (openAnimation < 1.0F) openAnimation += 0.06F;
            if (openAnimation > 1.0F) openAnimation = 1.0F;
            int w = maximized ? width : (int)(windowWidth * openAnimation);
            int h = maximized ? height : (int)(windowHeight * openAnimation);
            windowX = maximized ? 0 : (width - w) / 2;
            windowY = maximized ? 0 : (height - h) / 2;
            if (dragging) { windowX = mouseX - dragOffsetX; windowY = mouseY - dragOffsetY; }

            if (minimized) {
                drawRoundedRect(0, 0, 130, 24, 6, new Color(28, 28, 32, 255).getRGB());
                drawRect(0, 4, 2, 20, ACCENT_WHITE.getRGB());
                mc.fontRendererObj.drawStringWithShadow("Augustus", 10, 8, TITLE_TEXT.getRGB());
                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }

            // Rounded shadow
            drawRoundedRect(windowX + 3, windowY + 3, windowX + w + 3, windowY + h + 3, 12, new Color(0, 0, 0, 110).getRGB());
            // Rounded main window
            drawRoundedRect(windowX, windowY, windowX + w, windowY + h, 12, WINDOW_BG.getRGB());
            // Rounded outline
            drawRoundedOutline(windowX, windowY, windowX + w, windowY + h, 12, OUTLINE.getRGB());
            // Rounded title bar
            drawRoundedRect(windowX, windowY, windowX + w, windowY + 28, 12, TITLE_BAR.getRGB());
            drawRect(windowX, windowY + 14, windowX + w, windowY + 28, TITLE_BAR.getRGB());
            drawRect(windowX + 2, windowY + 4, windowX + 3, windowY + 24, ACCENT_WHITE.getRGB());

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.4, 1.4, 1.0);
            mc.fontRendererObj.drawStringWithShadow("Augustus", (windowX + 12) / 1.4f, (windowY + 8) / 1.4f, TITLE_TEXT.getRGB());
            GlStateManager.popMatrix();

            int cx = windowX + w - 75;
            int cy = windowY + 7;
            drawControlButton(cx, cy, 16, 14, "_", mouseX, mouseY);
            drawControlButton(cx + 20, cy, 16, 14, "□", mouseX, mouseY);
            drawControlButton(cx + 40, cy, 16, 14, "X", mouseX, mouseY);

            // Square tabs (only window is rounded)
            int tx = windowX + 14;
            int ty = windowY + 34;
            for (int i = 0; i < categoryNames.length; i++) {
                int tw = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 18;
                boolean active = i == activeTab;
                boolean tabHovered = mouseX >= tx && mouseX <= tx + tw && mouseY >= ty && mouseY <= ty + 18;
                Color tabColor = active ? TAB_ACTIVE : (tabHovered ? TAB_HOVER : TAB_INACTIVE);
                drawRect(tx, ty, tx + tw, ty + 18, tabColor.getRGB());
                if (active) drawRect(tx + 4, ty + 17, tx + tw - 4, ty + 18, ACCENT_WHITE.getRGB());
                int textX = tx + (tw - mc.fontRendererObj.getStringWidth(categoryNames[i])) / 2;
                mc.fontRendererObj.drawString(categoryNames[i], textX, ty + 5, active ? new Color(255, 255, 255, 255).getRGB() : new Color(130, 130, 136, 255).getRGB());
                tx += tw + 4;
            }

            int dWheel = Mouse.getDWheel();
            if (dWheel > 0) scrollOffset = Math.max(0, scrollOffset - 1);
            if (dWheel < 0 && scrollOffset < maxScroll) scrollOffset++;

            List<Module> modules = getModules(activeTab);
            maxScroll = Math.max(0, modules.size() - 18);
            if (scrollOffset > maxScroll) scrollOffset = maxScroll;

            int mx = windowX + 14;
            int my = windowY + 56 - (scrollOffset * 14);
            hoveredModule = null;

            for (Module m : modules) {
                if (my < windowY + 54) { my += 14; continue; }
                if (my > windowY + h - 20) break;
                boolean hovered = mouseX >= mx && mouseX <= mx + 115 && mouseY >= my && mouseY <= my + 12;
                boolean selected = m == selectedModule;

                if (hovered || selected) {
                    drawRect(mx - 4, my - 1, mx + 115, my + 13, HOVER_BG.getRGB());
                    if (selected) drawRect(mx - 4, my - 1, mx - 2, my + 13, ACCENT_WHITE.getRGB());
                    if (hovered) hoveredModule = m;
                }
                if (m.isEnabled()) {
                    drawRect(mx - 4, my - 1, mx + 115, my + 13, MODULE_TOGGLED_BG.getRGB());
                    if (selected) drawRect(mx - 4, my - 1, mx - 2, my + 13, ACCENT_WHITE.getRGB());
                }

                Color moduleColor = m.isEnabled() ? MODULE_ENABLED : MODULE_DISABLED;
                mc.fontRendererObj.drawStringWithShadow(m.getName(), mx, my + 2, moduleColor.getRGB());

                if (selected && m.getSettings().size() > 0) {
                    int sx = mx + 122;
                    int sy = my - (m.getSettings().size() / 2) * 13;
                    for (Module.Setting setting : m.getSettings()) {
                        if (sy < windowY + 54) { sy += 13; continue; }
                        if (sy > windowY + h - 20) break;
                        String sn = setting.getName();
                        String sv = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();
                        mc.fontRendererObj.drawString(sn + ":", sx, sy + 2, SETTING_LABEL.getRGB());
                        int vx = sx + mc.fontRendererObj.getStringWidth(sn) + 12;
                        drawRect(vx - 3, sy, vx + mc.fontRendererObj.getStringWidth(sv) + 6, sy + 11, new Color(20, 20, 23, 255).getRGB());
                        mc.fontRendererObj.drawString(sv, vx, sy + 2, setting.isSlider() ? SETTING_VALUE_SLIDER.getRGB() : SETTING_VALUE.getRGB());
                        sy += 13;
                    }
                }
                my += 14;
            }

            if (maxScroll > 0) {
                int sbX = windowX + w - 7;
                int sbH = h - 70;
                drawRect(sbX, windowY + 56, sbX + 4, windowY + 56 + sbH, SCROLLBAR_TRACK.getRGB());
                int thumbH = Math.max(20, sbH / (maxScroll + 1));
                int thumbY = windowY + 56 + (int)((double)scrollOffset / maxScroll * (sbH - thumbH));
                drawRect(sbX, thumbY, sbX + 4, thumbY + thumbH, SCROLLBAR_THUMB.getRGB());
            }

            if (hoveredModule != null && selectedModule != hoveredModule) {
                drawTooltip(hoveredModule, mouseX, mouseY);
            }

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        private void drawIcons(int mouseX, int mouseY) {
            int iconSize = 16;
            int gap = 10;
            int totalWidth = iconSize * 2 + gap;
            int startX = (width - totalWidth) / 2;
            int y = 5;

            boolean hover1 = mouseX >= startX && mouseX <= startX + iconSize && mouseY >= y && mouseY <= y + iconSize;
            drawRect(startX, y, startX + iconSize, y + iconSize, hover1 ? CONTROL_BUTTON_HOVER.getRGB() : CONTROL_BUTTON.getRGB());
            mc.fontRendererObj.drawStringWithShadow("≡", startX + 4, y + 4, TITLE_TEXT.getRGB());

            int paperX = startX + iconSize + gap;
            boolean hover2 = mouseX >= paperX && mouseX <= paperX + iconSize && mouseY >= y && mouseY <= y + iconSize;
            drawRect(paperX, y, paperX + iconSize, y + iconSize, hover2 ? CONTROL_BUTTON_HOVER.getRGB() : CONTROL_BUTTON.getRGB());
            mc.fontRendererObj.drawStringWithShadow("⚙", paperX + 2, y + 4, TITLE_TEXT.getRGB());
        }

        private void drawControlButton(int x, int y, int w, int h, String icon, int mx, int my) {
            boolean hovered = mx >= x && mx <= x + w && my >= y && my <= y + h;
            drawRect(x, y, x + w, y + h, hovered ? CONTROL_BUTTON_HOVER.getRGB() : CONTROL_BUTTON.getRGB());
            int iconX = x + (w - mc.fontRendererObj.getStringWidth(icon)) / 2;
            mc.fontRendererObj.drawString(icon, iconX, y + (h - 8) / 2, new Color(255, 255, 255, 255).getRGB());
        }

        private void drawRoundedRect(int x1, int y1, int x2, int y2, int radius, int color) {
            drawRect(x1 + radius, y1, x2 - radius, y2, color);
            drawRect(x1, y1 + radius, x2, y2 - radius, color);
            drawRect(x1 + radius, y1 + radius, x2 - radius, y2 - radius, color);
            drawRect(x1, y1 + radius, x1 + radius, y2 - radius, color);
            drawRect(x2 - radius, y1 + radius, x2, y2 - radius, color);
            // corners filled as squares is not perfectly round but acceptable for "rounded" look
        }

        private void drawRoundedOutline(int x1, int y1, int x2, int y2, int radius, int color) {
            drawRect(x1, y1 + radius, x1 + 1, y2 - radius, color);
            drawRect(x2 - 1, y1 + radius, x2, y2 - radius, color);
            drawRect(x1 + radius, y1, x2 - radius, y1 + 1, color);
            drawRect(x1 + radius, y2 - 1, x2 - radius, y2, color);
        }

        private void drawTooltip(Module m, int mouseX, int mouseY) {
            String[] words = m.getDescription().split(" ");
            List<String> lines = new ArrayList<>();
            String current = "";
            for (String word : words) {
                if (mc.fontRendererObj.getStringWidth(current + word) < 150) {
                    current += word + " ";
                } else {
                    lines.add(current.trim());
                    current = word + " ";
                }
            }
            if (!current.trim().isEmpty()) lines.add(current.trim());
            int tw = 0;
            for (String line : lines) tw = Math.max(tw, mc.fontRendererObj.getStringWidth(line));
            tw += 14;
            int th = lines.size() * 11 + 10;
            int tipX = mouseX + 12;
            int tipY = mouseY + 12;
            if (tipX + tw > mc.displayWidth / 2) tipX = mouseX - tw - 12;
            if (tipY + th > mc.displayHeight / 2) tipY = mouseY - th - 12;
            drawRect(tipX, tipY, tipX + tw, tipY + th, TOOLTIP_BG.getRGB());
            drawRect(tipX, tipY, tipX + tw, tipY + 1, TOOLTIP_BORDER.getRGB());
            drawRect(tipX, tipY + th - 1, tipX + tw, tipY + th, TOOLTIP_BORDER.getRGB());
            drawRect(tipX, tipY, tipX + 1, tipY + th, TOOLTIP_BORDER.getRGB());
            drawRect(tipX + tw - 1, tipY, tipX + tw, tipY + th, TOOLTIP_BORDER.getRGB());
            for (int i = 0; i < lines.size(); i++) {
                mc.fontRendererObj.drawStringWithShadow(lines.get(i), tipX + 7, tipY + 5 + i * 11, TOOLTIP_TEXT.getRGB());
            }
        }

        private List<Module> getModules(int tab) {
            List<Module> result = new ArrayList<>();
            Module.Category cat = Module.Category.values()[tab];
            for (Module m : AmongusClient.moduleManager.getModules()) {
                if (m.getCategory() == cat) {
                    result.add(m);
                }
            }
            return result;
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            int iconSize = 16;
            int gap = 10;
            int totalWidth = iconSize * 2 + gap;
            int startX = (width - totalWidth) / 2;
            int y = 5;

            if (mouseButton == 0 && mouseX >= startX && mouseX <= startX + iconSize && mouseY >= y && mouseY <= y + iconSize) {
                windowVisible = !windowVisible;
                return;
            }

            int paperX = startX + iconSize + gap;
            if (mouseButton == 0 && mouseX >= paperX && mouseX <= paperX + iconSize && mouseY >= y && mouseY <= y + iconSize) {
                ConfigManager.saveConfig();
                return;
            }

            if (!windowVisible) return;

            if (minimized) {
                if (mouseX >= 0 && mouseX <= 130 && mouseY >= 0 && mouseY <= 24) minimized = false;
                return;
            }
            int w = maximized ? width : windowWidth;
            int h = maximized ? height : windowHeight;
            int cx = windowX + w - 75;
            int cy = windowY + 7;
            if (mouseX >= cx && mouseX <= cx + 16 && mouseY >= cy && mouseY <= cy + 14) { minimized = true; windowVisible = false; return; }
            if (mouseX >= cx + 20 && mouseX <= cx + 36 && mouseY >= cy && mouseY <= cy + 14) { maximized = !maximized; return; }
            if (mouseX >= cx + 40 && mouseX <= cx + 56 && mouseY >= cy && mouseY <= cy + 14) { windowVisible = false; return; }
            if (mouseY >= windowY && mouseY <= windowY + 28 && mouseX >= windowX && mouseX <= windowX + w - 75) { dragging = true; dragOffsetX = mouseX - windowX; dragOffsetY = mouseY - windowY; return; }

            int tx = windowX + 14;
            int ty = windowY + 34;
            for (int i = 0; i < categoryNames.length; i++) {
                int tw = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 18;
                if (mouseX >= tx && mouseX <= tx + tw && mouseY >= ty && mouseY <= ty + 18) { activeTab = i; selectedModule = null; return; }
                tx += tw + 4;
            }

            List<Module> modules = getModules(activeTab);
            int mx = windowX + 14;
            int my = windowY + 56 - (scrollOffset * 14);
            for (Module m : modules) {
                if (my < windowY + 54) { my += 14; continue; }
                if (my > windowY + h - 20) break;
                if (mouseX >= mx && mouseX <= mx + 115 && mouseY >= my && mouseY <= my + 12) {
                    if (mouseButton == 0) { m.toggle(); return; }
                    if (mouseButton == 1) { selectedModule = (selectedModule == m) ? null : m; return; }
                }
                my += 14;
            }

            if (selectedModule != null) {
                int idx = modules.indexOf(selectedModule);
                if (idx >= 0) {
                    int sx = mx + 122;
                    int sy = windowY + 56 - (scrollOffset * 14) + (idx * 14) - (selectedModule.getSettings().size() / 2) * 13;
                    for (Module.Setting setting : selectedModule.getSettings()) {
                        int vx = sx + mc.fontRendererObj.getStringWidth(setting.getName()) + 12;
                        String sv = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();
                        int vw = mc.fontRendererObj.getStringWidth(sv);
                        if (mouseX >= vx - 3 && mouseX <= vx + vw + 6 && mouseY >= sy && mouseY <= sy + 11) {
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
