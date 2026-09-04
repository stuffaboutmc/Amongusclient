package com.amongus.client.modules.render;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import com.amongus.client.utils.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Module {
    private static final Color WINDOW_BG = new Color(32, 32, 37, 180);
    private static final Color TITLE_BAR = new Color(16, 16, 18, 180);
    private static final Color TAB_ACTIVE = new Color(55, 55, 62, 255);
    private static final Color TAB_INACTIVE = new Color(24, 24, 28, 255);
    private static final Color MODULE_ENABLED = new Color(235, 235, 240, 255);
    private static final Color MODULE_DISABLED = new Color(120, 120, 126, 255);
    private static final Color MODULE_TOGGLED_BG = new Color(14, 14, 16, 255);
    private static final Color SETTING_LABEL = new Color(115, 115, 120, 255);
    private static final Color SETTING_VALUE = new Color(245, 245, 250, 255);
    private static final Color SETTING_VALUE_SLIDER = new Color(255, 210, 80, 255);
    private static final Color OUTLINE = new Color(52, 52, 58, 255);
    private static final Color TITLE_TEXT = new Color(255, 255, 255, 255);
    private static final Color ACCENT = new Color(0, 200, 120, 255);
    private static final Color CONTROL_BUTTON = new Color(68, 68, 74, 255);
    private static final Color SCROLLBAR_TRACK = new Color(20, 20, 24, 255);
    private static final Color SCROLLBAR_THUMB = new Color(75, 75, 82, 255);
    private static final Color SLIDER_TRACK = new Color(70, 70, 76, 255);
    private static final Color SLIDER_FILL = new Color(16, 16, 18, 255);
    private static final Color SLIDER_THUMB = new Color(230, 230, 235, 255);

    private static File tabFile;

    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER, "Opens the panel menu.");
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new AugustusGuiScreen());
    }

    private static void saveTab(int tab) {
        try {
            if (tabFile == null) tabFile = new File(Minecraft.getMinecraft().mcDataDir, "amongus/tab.txt");
            if (!tabFile.getParentFile().exists()) tabFile.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(tabFile);
            writer.write(String.valueOf(tab));
            writer.close();
        } catch (Exception e) {}
    }

    private static int loadTab() {
        try {
            if (tabFile == null) tabFile = new File(Minecraft.getMinecraft().mcDataDir, "amongus/tab.txt");
            if (!tabFile.exists()) return 0;
            BufferedReader reader = new BufferedReader(new FileReader(tabFile));
            String line = reader.readLine();
            reader.close();
            return Integer.parseInt(line.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public class AugustusGuiScreen extends GuiScreen {
        private String[] categoryNames = {"Combat", "Movement", "Render", "Player", "Misc"};
        private int activeTab = 0;
        private Module selectedModule = null;
        private int moduleScrollOffset = 0;
        private int settingsScrollOffset = 0;
        private int maxModuleScroll = 0;
        private int maxSettingsScroll = 0;
        private int windowWidth = 660;
        private int windowHeight = 460;
        private boolean minimized = false;
        private boolean maximized = false;
        private boolean dragging = false;
        private int dragOffsetX, dragOffsetY;
        private int windowX, windowY;
        private boolean windowVisible = true;
        private Module.Setting draggedSlider = null;
        private int sliderTrackX, sliderTrackY, sliderTrackWidth;

        private float highlightX = 0;
        private float highlightWidth = 0;
        private boolean animationInitialized = false;

        private final int MODULE_PANEL_X = 14;
        private final int MODULE_PANEL_WIDTH = 160;
        private final int SETTINGS_PANEL_X = 184;
        private final int PANEL_TOP = 66;
        private final int PANEL_BOTTOM = 20;

        public AugustusGuiScreen() {
            activeTab = loadTab();
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawIcons(mouseX, mouseY);

            if (!windowVisible) {
                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }

            int w = maximized ? width : windowWidth;
            int h = maximized ? height : windowHeight;
            windowX = maximized ? 0 : (width - w) / 2;
            windowY = maximized ? 0 : (height - h) / 2;
            if (dragging) { windowX = mouseX - dragOffsetX; windowY = mouseY - dragOffsetY; }

            if (minimized) {
                drawRect(0, 0, 130, 26, new Color(16,16,18,180).getRGB());
                drawRect(0, 5, 3, 21, ACCENT.getRGB());
                mc.fontRendererObj.drawStringWithShadow("Augustus", 12, 9, TITLE_TEXT.getRGB());
                super.drawScreen(mouseX, mouseY, partialTicks);
                return;
            }

            drawSmoothRoundedRect(windowX + 4, windowY + 4, windowX + w + 4, windowY + h + 4, 12, new Color(0,0,0,100).getRGB());
            drawSmoothRoundedRect(windowX, windowY, windowX + w, windowY + h, 12, WINDOW_BG.getRGB());
            drawSmoothRoundedOutline(windowX, windowY, windowX + w, windowY + h, 12, OUTLINE.getRGB());

            drawSmoothRoundedRect(windowX, windowY, windowX + w, windowY + 30, 12, TITLE_BAR.getRGB());
            drawRect(windowX, windowY + 15, windowX + w, windowY + 30, TITLE_BAR.getRGB());
            drawRect(windowX + 4, windowY + 4, windowX + 6, windowY + 26, ACCENT.getRGB());

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.4, 1.4, 1.0);
            mc.fontRendererObj.drawStringWithShadow("Augustus", (windowX + 16) / 1.4f, (windowY + 9) / 1.4f, TITLE_TEXT.getRGB());
            GlStateManager.popMatrix();

            int cx = windowX + w - 80;
            int cy = windowY + 8;
            drawControlButton(cx, cy, 16, 14, "_", mouseX, mouseY);
            drawControlButton(cx + 20, cy, 16, 14, "□", mouseX, mouseY);
            drawControlButton(cx + 40, cy, 16, 14, "X", mouseX, mouseY);

            int tx = windowX + 16;
            int ty = windowY + 38;
            float targetX = 0;
            float targetWidth = 0;

            for (int i = 0; i < categoryNames.length; i++) {
                int tw = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 20;
                if (i == activeTab) {
                    targetX = tx;
                    targetWidth = tw;
                }
                tx += tw + 4;
            }

            if (!animationInitialized) {
                highlightX = targetX;
                highlightWidth = targetWidth;
                animationInitialized = true;
            } else {
                highlightX += (targetX - highlightX) * 0.3f;
                highlightWidth += (targetWidth - highlightWidth) * 0.3f;
            }

            tx = windowX + 16;
            for (int i = 0; i < categoryNames.length; i++) {
                int tw = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 20;
                boolean active = i == activeTab;
                drawRect(tx, ty, tx + tw, ty + 20, active ? TAB_ACTIVE.getRGB() : TAB_INACTIVE.getRGB());
                int textX = tx + (tw - mc.fontRendererObj.getStringWidth(categoryNames[i])) / 2;
                mc.fontRendererObj.drawString(categoryNames[i], textX, ty + 6, active ? new Color(255,255,255,255).getRGB() : new Color(135,135,140,255).getRGB());
                tx += tw + 4;
            }

            drawRect((int)highlightX + 4, ty + 18, (int)(highlightX + highlightWidth) - 4, ty + 20, ACCENT.getRGB());

            int dWheel = Mouse.getDWheel();
            if (dWheel != 0) {
                if (mouseX >= windowX + MODULE_PANEL_X && mouseX <= windowX + MODULE_PANEL_X + MODULE_PANEL_WIDTH) {
                    if (dWheel > 0) moduleScrollOffset = Math.max(0, moduleScrollOffset - 1);
                    else if (moduleScrollOffset < maxModuleScroll) moduleScrollOffset++;
                } else if (mouseX >= windowX + SETTINGS_PANEL_X && mouseX <= windowX + w - 10) {
                    if (dWheel > 0) settingsScrollOffset = Math.max(0, settingsScrollOffset - 1);
                    else if (settingsScrollOffset < maxSettingsScroll) settingsScrollOffset++;
                }
            }

            List<Module> modules = getModules(activeTab);
            maxModuleScroll = Math.max(0, modules.size() - 18);
            moduleScrollOffset = Math.min(moduleScrollOffset, maxModuleScroll);

            int moduleY = windowY + PANEL_TOP - (moduleScrollOffset * 15);
            for (Module m : modules) {
                if (moduleY < windowY + PANEL_TOP - 15) { moduleY += 15; continue; }
                if (moduleY > windowY + h - PANEL_BOTTOM) break;
                int mx = windowX + MODULE_PANEL_X;

                if (m.isEnabled()) {
                    drawRect(mx - 2, moduleY - 1, mx + MODULE_PANEL_WIDTH, moduleY + 13, MODULE_TOGGLED_BG.getRGB());
                }
                if (m == selectedModule) {
                    drawRect(mx - 2, moduleY - 1, mx, moduleY + 13, ACCENT.getRGB());
                }

                Color moduleColor = m.isEnabled() ? MODULE_ENABLED : MODULE_DISABLED;
                mc.fontRendererObj.drawStringWithShadow(m.getName(), mx + 2, moduleY + 2, moduleColor.getRGB());
                moduleY += 15;
            }

            if (selectedModule != null) {
                int settingsX = windowX + SETTINGS_PANEL_X;
                List<Module.Setting> settings = selectedModule.getSettings();
                maxSettingsScroll = Math.max(0, settings.size() - 20);
                settingsScrollOffset = Math.min(settingsScrollOffset, maxSettingsScroll);

                int settingY = windowY + PANEL_TOP - (settingsScrollOffset * 15);
                for (Module.Setting setting : settings) {
                    if (settingY < windowY + PANEL_TOP - 15) { settingY += 15; continue; }
                    if (settingY > windowY + h - PANEL_BOTTOM) break;

                    if (!isSettingVisible(selectedModule, setting)) {
                        settingY += 15;
                        continue;
                    }

                    if (setting.isSlider()) {
                        String name = setting.getName();
                        double value = setting.getDoubleValue();
                        double min = setting.getMin();
                        double max = setting.getMax();
                        double ratio = (value - min) / (max - min);

                        int labelWidth = mc.fontRendererObj.getStringWidth(name + ":");
                        int trackX = settingsX + labelWidth + 10;
                        int trackY = settingY + 3;
                        int trackWidth = 130;
                        int trackHeight = 7;

                        if (draggedSlider == setting) {
                            double newRatio = (mouseX - trackX) / (double) trackWidth;
                            newRatio = Math.max(0, Math.min(1, newRatio));
                            setting.setValue(String.valueOf(min + newRatio * (max - min)));
                        }

                        drawSmoothRoundedRect(trackX, trackY, trackX + trackWidth, trackY + trackHeight, 3, SLIDER_TRACK.getRGB());
                        int fillWidth = (int)(ratio * trackWidth);
                        if (fillWidth > 0) {
                            drawSmoothRoundedRect(trackX, trackY, trackX + fillWidth, trackY + trackHeight, 3, SLIDER_FILL.getRGB());
                        }
                        int thumbX = trackX + (int)(ratio * trackWidth) - 3;
                        drawSmoothRoundedRect(thumbX, trackY - 1, thumbX + 4, trackY + trackHeight + 1, 2, SLIDER_THUMB.getRGB());

                        mc.fontRendererObj.drawString(name + ":", settingsX, settingY + 1, SETTING_LABEL.getRGB());
                        String valueStr = String.valueOf(value);
                        int valueX = trackX + trackWidth + 8;
                        mc.fontRendererObj.drawString(valueStr, valueX, settingY + 1, SETTING_VALUE_SLIDER.getRGB());

                        sliderTrackX = trackX;
                        sliderTrackY = trackY;
                        sliderTrackWidth = trackWidth;
                    } else if (!setting.isSlider() && setting.getOptions() != null && setting.getOptions().length > 2) {
                        mc.fontRendererObj.drawStringWithShadow(setting.getName() + ":", settingsX, settingY + 2, SETTING_LABEL.getRGB());
                        int pillX = settingsX + mc.fontRendererObj.getStringWidth(setting.getName()) + 12;
                        for (String option : setting.getOptions()) {
                            int pillWidth = mc.fontRendererObj.getStringWidth(option) + 10;
                            boolean isSelected = setting.getValue().equals(option);
                            drawRect(pillX, settingY, pillX + pillWidth, settingY + 12, isSelected ? MODULE_ENABLED.getRGB() : new Color(28,28,34,255).getRGB());
                            mc.fontRendererObj.drawString(option, pillX + 5, settingY + 2, isSelected ? new Color(0,0,0,255).getRGB() : SETTING_VALUE.getRGB());
                            pillX += pillWidth + 5;
                        }
                    } else {
                        String sn = setting.getName();
                        String sv = setting.getValue();
                        mc.fontRendererObj.drawString(sn + ":", settingsX, settingY + 2, SETTING_LABEL.getRGB());
                        int vx = settingsX + mc.fontRendererObj.getStringWidth(sn) + 12;
                        drawRect(vx - 2, settingY, vx + mc.fontRendererObj.getStringWidth(sv) + 4, settingY + 12, new Color(18,18,22,255).getRGB());
                        mc.fontRendererObj.drawString(sv, vx, settingY + 2, SETTING_VALUE.getRGB());
                    }
                    settingY += 15;
                }
            }

            if (maxModuleScroll > 0) {
                drawScrollbar(windowX + MODULE_PANEL_X + MODULE_PANEL_WIDTH + 8, windowY + PANEL_TOP, h - PANEL_TOP - PANEL_BOTTOM, moduleScrollOffset, maxModuleScroll);
            }
            if (maxSettingsScroll > 0) {
                drawScrollbar(windowX + w - 10, windowY + PANEL_TOP, h - PANEL_TOP - PANEL_BOTTOM, settingsScrollOffset, maxSettingsScroll);
            }

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        private void drawScrollbar(int x, int y, int totalHeight, int offset, int maxScroll) {
            drawSmoothRoundedRect(x, y, x + 3, y + totalHeight, 2, SCROLLBAR_TRACK.getRGB());
            int thumbHeight = Math.max(18, totalHeight / (maxScroll + 1));
            int thumbY = y + (int)((double)offset / maxScroll * (totalHeight - thumbHeight));
            drawSmoothRoundedRect(x, thumbY, x + 3, thumbY + thumbHeight, 2, SCROLLBAR_THUMB.getRGB());
        }

        private void drawIcons(int mouseX, int mouseY) {
            int iconSize = 18;
            int gap = 12;
            int totalWidth = iconSize * 2 + gap;
            int startX = (width - totalWidth) / 2;
            int y = 6;

            drawRect(startX, y, startX + iconSize, y + iconSize, CONTROL_BUTTON.getRGB());
            mc.fontRendererObj.drawStringWithShadow("≡", startX + 5, y + 5, TITLE_TEXT.getRGB());

            int paperX = startX + iconSize + gap;
            drawRect(paperX, y, paperX + iconSize, y + iconSize, CONTROL_BUTTON.getRGB());
            mc.fontRendererObj.drawStringWithShadow("⚙", paperX + 2, y + 5, TITLE_TEXT.getRGB());
        }

        private void drawControlButton(int x, int y, int w, int h, String icon, int mx, int my) {
            drawRect(x, y, x + w, y + h, CONTROL_BUTTON.getRGB());
            int iconX = x + (w - mc.fontRendererObj.getStringWidth(icon)) / 2;
            mc.fontRendererObj.drawString(icon, iconX, y + (h - 8) / 2, new Color(255,255,255,255).getRGB());
        }

        private void drawSmoothRoundedRect(int x1, int y1, int x2, int y2, int radius, int color) {
            if (x2 - x1 < radius * 2 || y2 - y1 < radius * 2) {
                drawRect(x1, y1, x2, y2, color);
                return;
            }
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            float a = ((color >> 24) & 0xFF) / 255f;
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;
            GL11.glColor4f(r, g, b, a);
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            GL11.glVertex2f(x1 + radius, y1 + radius);
            for (int i = 0; i <= 90; i++) {
                double angle = Math.toRadians(i);
                GL11.glVertex2f(x1 + radius - (float)(radius * Math.cos(angle)), y1 + radius - (float)(radius * Math.sin(angle)));
            }
            GL11.glVertex2f(x2 - radius, y1 + radius);
            for (int i = 0; i <= 90; i++) {
                double angle = Math.toRadians(i);
                GL11.glVertex2f(x2 - radius + (float)(radius * Math.cos(angle)), y1 + radius - (float)(radius * Math.sin(angle)));
            }
            GL11.glVertex2f(x2 - radius, y2 - radius);
            for (int i = 0; i <= 90; i++) {
                double angle = Math.toRadians(i);
                GL11.glVertex2f(x2 - radius + (float)(radius * Math.cos(angle)), y2 - radius + (float)(radius * Math.sin(angle)));
            }
            GL11.glVertex2f(x1 + radius, y2 - radius);
            for (int i = 0; i <= 90; i++) {
                double angle = Math.toRadians(i);
                GL11.glVertex2f(x1 + radius - (float)(radius * Math.cos(angle)), y2 - radius + (float)(radius * Math.sin(angle)));
            }
            GL11.glEnd();
            drawRect(x1 + radius, y1, x2 - radius, y1 + radius, color);
            drawRect(x1 + radius, y2 - radius, x2 - radius, y2, color);
            drawRect(x1, y1 + radius, x1 + radius, y2 - radius, color);
            drawRect(x2 - radius, y1 + radius, x2, y2 - radius, color);
            drawRect(x1 + radius, y1 + radius, x2 - radius, y2 - radius, color);
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }

        private void drawSmoothRoundedOutline(int x1, int y1, int x2, int y2, int radius, int color) {
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            float a = ((color >> 24) & 0xFF) / 255f;
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;
            GL11.glColor4f(r, g, b, a);
            GL11.glLineWidth(1.0f);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            for (int i = 0; i <= 90; i++) {
                double angle = Math.toRadians(i);
                GL11.glVertex2f(x1 + radius - (float)(radius * Math.cos(angle)), y1 + radius - (float)(radius * Math.sin(angle)));
            }
            for (int i = 0; i <= 90; i++) {
                double angle = Math.toRadians(i);
                GL11.glVertex2f(x2 - radius + (float)(radius * Math.cos(angle)), y1 + radius - (float)(radius * Math.sin(angle)));
            }
            for (int i = 0; i <= 90; i++) {
                double angle = Math.toRadians(i);
                GL11.glVertex2f(x2 - radius + (float)(radius * Math.cos(angle)), y2 - radius + (float)(radius * Math.sin(angle)));
            }
            for (int i = 0; i <= 90; i++) {
                double angle = Math.toRadians(i);
                GL11.glVertex2f(x1 + radius - (float)(radius * Math.cos(angle)), y2 - radius + (float)(radius * Math.sin(angle)));
            }
            GL11.glEnd();
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }

        private List<Module> getModules(int tab) {
            List<Module> result = new ArrayList<>();
            Module.Category cat = Module.Category.values()[tab];
            for (Module m : AmongusClient.moduleManager.getModules()) {
                if (m.getCategory() == cat) result.add(m);
            }
            return result;
        }

        // Hide settings based on parent mode
        private boolean isSettingVisible(Module mod, Module.Setting setting) {
            String name = setting.getName();

            // AntiBot checks: only visible when AntiBot mode is Custom
            if (name.startsWith("Check")) {
                Module.Setting antiBot = mod.getSetting("AntiBot");
                return antiBot != null && antiBot.getValue().equals("Custom");
            }

            // Rotation custom parameters: only visible when Rotation mode is Custom
            if (name.startsWith("Custom") && (name.contains("Speed") || name.contains("Acceleration") || name.contains("Noise"))) {
                Module.Setting rotation = mod.getSetting("Rotation");
                return rotation != null && rotation.getValue().equals("Custom");
            }

            return true;
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            int iconSize = 18;
            int gap = 12;
            int totalWidth = iconSize * 2 + gap;
            int startX = (width - totalWidth) / 2;
            int y = 6;

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
                if (mouseX >= 0 && mouseX <= 130 && mouseY >= 0 && mouseY <= 26) minimized = false;
                return;
            }

            int w = maximized ? width : windowWidth;
            int h = maximized ? height : windowHeight;
            int cx = windowX + w - 80;
            int cy = windowY + 8;
            if (mouseX >= cx && mouseX <= cx + 16 && mouseY >= cy && mouseY <= cy + 14) { minimized = true; windowVisible = false; return; }
            if (mouseX >= cx + 20 && mouseX <= cx + 36 && mouseY >= cy && mouseY <= cy + 14) { maximized = !maximized; return; }
            if (mouseX >= cx + 40 && mouseX <= cx + 56 && mouseY >= cy && mouseY <= cy + 14) { windowVisible = false; return; }
            if (mouseY >= windowY && mouseY <= windowY + 30 && mouseX >= windowX && mouseX <= windowX + w - 80) { dragging = true; dragOffsetX = mouseX - windowX; dragOffsetY = mouseY - windowY; return; }

            int tx = windowX + 16;
            int ty = windowY + 38;
            for (int i = 0; i < categoryNames.length; i++) {
                int tw = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 20;
                if (mouseX >= tx && mouseX <= tx + tw && mouseY >= ty && mouseY <= ty + 20) {
                    activeTab = i;
                    saveTab(activeTab);
                    animationInitialized = false;
                    selectedModule = null;
                    return;
                }
                tx += tw + 4;
            }

            List<Module> modules = getModules(activeTab);
            int mx = windowX + MODULE_PANEL_X;
            int my = windowY + PANEL_TOP - (moduleScrollOffset * 15);
            for (Module m : modules) {
                if (my < windowY + PANEL_TOP - 15) { my += 15; continue; }
                if (my > windowY + h - PANEL_BOTTOM) break;
                if (mouseX >= mx && mouseX <= mx + MODULE_PANEL_WIDTH && mouseY >= my && mouseY <= my + 13) {
                    if (mouseButton == 0) { m.toggle(); return; }
                    if (mouseButton == 1) { selectedModule = (selectedModule == m) ? null : m; return; }
                }
                my += 15;
            }

            if (selectedModule != null) {
                int settingsX = windowX + SETTINGS_PANEL_X;
                int settingY = windowY + PANEL_TOP - (settingsScrollOffset * 15);
                for (Module.Setting setting : selectedModule.getSettings()) {
                    if (settingY < windowY + PANEL_TOP - 15) { settingY += 15; continue; }
                    if (settingY > windowY + h - PANEL_BOTTOM) break;

                    if (!isSettingVisible(selectedModule, setting)) {
                        settingY += 15;
                        continue;
                    }

                    if (setting.isSlider()) {
                        int labelWidth = mc.fontRendererObj.getStringWidth(setting.getName() + ":");
                        int trackX = settingsX + labelWidth + 10;
                        int trackY = settingY + 3;
                        int trackWidth = 130;
                        if (mouseX >= trackX && mouseX <= trackX + trackWidth && mouseY >= trackY && mouseY <= trackY + 7) {
                            draggedSlider = setting;
                            double min = setting.getMin();
                            double max = setting.getMax();
                            double ratio = (mouseX - trackX) / (double) trackWidth;
                            ratio = Math.max(0, Math.min(1, ratio));
                            setting.setValue(String.valueOf(min + ratio * (max - min)));
                            return;
                        }
                    } else if (!setting.isSlider() && setting.getOptions() != null && setting.getOptions().length > 2) {
                        int pillX = settingsX + mc.fontRendererObj.getStringWidth(setting.getName()) + 12;
                        for (String option : setting.getOptions()) {
                            int pillWidth = mc.fontRendererObj.getStringWidth(option) + 10;
                            if (mouseX >= pillX && mouseX <= pillX + pillWidth && mouseY >= settingY && mouseY <= settingY + 12) {
                                setting.setValue(option);
                                return;
                            }
                            pillX += pillWidth + 5;
                        }
                    } else {
                        String sn = setting.getName();
                        int vx = settingsX + mc.fontRendererObj.getStringWidth(sn) + 12;
                        String sv = setting.getValue();
                        int vw = mc.fontRendererObj.getStringWidth(sv);
                        if (mouseX >= vx - 2 && mouseX <= vx + vw + 4 && mouseY >= settingY && mouseY <= settingY + 12) {
                            setting.cycle();
                            return;
                        }
                    }
                    settingY += 15;
                }
            }
        }

        @Override
        protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
            if (draggedSlider != null) {
                double min = draggedSlider.getMin();
                double max = draggedSlider.getMax();
                double ratio = (mouseX - sliderTrackX) / (double) sliderTrackWidth;
                ratio = Math.max(0, Math.min(1, ratio));
                draggedSlider.setValue(String.valueOf(min + ratio * (max - min)));
            }
            super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }

        @Override
        protected void mouseReleased(int mouseX, int mouseY, int state) {
            draggedSlider = null;
            dragging = false;
        }

        @Override
        public boolean doesGuiPauseGame() { return false; }
    }
}
