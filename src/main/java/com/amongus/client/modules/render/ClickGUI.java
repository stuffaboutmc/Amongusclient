package com.amongus.client.modules.render;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Module {
    private static final Color WINDOW_BG = new Color(45, 45, 50, 255);
    private static final Color TAB_ACTIVE = new Color(70, 70, 78, 255);
    private static final Color TAB_INACTIVE = new Color(35, 35, 40, 255);
    private static final Color MODULE_ENABLED = new Color(0, 255, 128, 255);
    private static final Color MODULE_DISABLED = new Color(170, 170, 170, 255);
    private static final Color SETTING_LABEL = new Color(140, 140, 145, 255);
    private static final Color SETTING_VALUE = new Color(255, 255, 255, 255);
    private static final Color SETTING_VALUE_SLIDER = new Color(255, 200, 50, 255);
    private static final Color OUTLINE = new Color(70, 70, 75, 255);
    private static final Color TITLE_TEXT = new Color(255, 255, 255, 255);
    private static final Color HOVER_BG = new Color(60, 60, 66, 255);
    private static final Color ACCENT_GREEN = new Color(0, 255, 128, 255);

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

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            if (openAnimation < 1.0F) openAnimation += 0.1F;
            if (openAnimation > 1.0F) openAnimation = 1.0F;

            int windowWidth = (int) (440 * openAnimation);
            int windowHeight = (int) (280 * openAnimation);
            int windowX = (width - windowWidth) / 2;
            int windowY = (height - windowHeight) / 2;

            // Window shadow
            drawRect(windowX + 3, windowY + 3, windowX + windowWidth + 3, windowY + windowHeight + 3, new Color(0, 0, 0, 80).getRGB());

            // Opaque gray window
            drawRect(windowX, windowY, windowX + windowWidth, windowY + windowHeight, WINDOW_BG.getRGB());
            drawRect(windowX, windowY, windowX + windowWidth, windowY + 1, OUTLINE.getRGB());
            drawRect(windowX, windowY + windowHeight - 1, windowX + windowWidth, windowY + windowHeight, OUTLINE.getRGB());
            drawRect(windowX, windowY, windowX + 1, windowY + windowHeight, OUTLINE.getRGB());
            drawRect(windowX + windowWidth - 1, windowY, windowX + windowWidth, windowY + windowHeight, OUTLINE.getRGB());

            // Augustus title with green accent
            drawRect(windowX, windowY, windowX + 3, windowY + 22, ACCENT_GREEN.getRGB());
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.4, 1.4, 1.0);
            mc.fontRendererObj.drawStringWithShadow("Augustus", (windowX + 10) / 1.4f, (windowY + 5) / 1.4f, TITLE_TEXT.getRGB());
            GlStateManager.popMatrix();

            // Chrome tabs
            int tabX = windowX + 80;
            int tabY = windowY + 4;
            for (int i = 0; i < categoryNames.length; i++) {
                String tabName = categoryNames[i];
                int tabWidth = mc.fontRendererObj.getStringWidth(tabName) + 16;
                int tabHeight = 14;
                boolean isActive = i == activeTab;

                // Tab background
                drawRect(tabX, tabY, tabX + tabWidth, tabY + tabHeight, isActive ? TAB_ACTIVE.getRGB() : TAB_INACTIVE.getRGB());
                drawRect(tabX, tabY, tabX + tabWidth, tabY + 1, OUTLINE.getRGB());
                drawRect(tabX, tabY, tabX + 1, tabY + tabHeight, OUTLINE.getRGB());
                drawRect(tabX + tabWidth - 1, tabY, tabX + tabWidth, tabY + tabHeight, OUTLINE.getRGB());

                // Active tab green underline
                if (isActive) {
                    drawRect(tabX, tabY + tabHeight - 1, tabX + tabWidth, tabY + tabHeight, ACCENT_GREEN.getRGB());
                }

                int tabTextX = tabX + (tabWidth - mc.fontRendererObj.getStringWidth(tabName)) / 2;
                mc.fontRendererObj.drawString(tabName, tabTextX, tabY + 4, isActive ? new Color(255, 255, 255, 255).getRGB() : new Color(150, 150, 155, 255).getRGB());

                tabX += tabWidth + 3;
            }

            // Scroll handling
            int dWheel = Mouse.getDWheel();
            if (dWheel > 0) scrollOffset = Math.max(0, scrollOffset - 1);
            if (dWheel < 0) scrollOffset++;

            // Module list
            List<Module> activeModules = getModulesInCategory(activeTab);
            int moduleX = windowX + 12;
            int moduleY = windowY + 28 - (scrollOffset * 13);

            for (Module m : activeModules) {
                if (moduleY < windowY + 25) { moduleY += 13; continue; }
                if (moduleY > windowY + windowHeight - 15) break;

                boolean hovered = mouseX >= moduleX && mouseX <= moduleX + 110 && mouseY >= moduleY && mouseY <= moduleY + 12;
                boolean isSelected = m == selectedModule;

                if (hovered || isSelected) {
                    drawRect(moduleX - 4, moduleY - 1, moduleX + 110, moduleY + 12, HOVER_BG.getRGB());
                    if (isSelected) {
                        drawRect(moduleX - 4, moduleY - 1, moduleX - 2, moduleY + 12, ACCENT_GREEN.getRGB());
                    }
                }

                Color moduleColor = m.isEnabled() ? MODULE_ENABLED : MODULE_DISABLED;
                mc.fontRendererObj.drawStringWithShadow(m.getName(), moduleX, moduleY + 2, moduleColor.getRGB());

                // Settings for selected module - one per line to the right
                if (isSelected && m.getSettings().size() > 0) {
                    int settingsX = moduleX + 120;
                    int settingsY = moduleY - (m.getSettings().size() / 2) * 12;

                    for (Module.Setting setting : m.getSettings()) {
                        if (settingsY < windowY + 25) { settingsY += 12; continue; }
                        if (settingsY > windowY + windowHeight - 15) break;

                        String settingName = setting.getName();
                        String settingValue = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();

                        // Setting label
                        mc.fontRendererObj.drawString(settingName + ":", settingsX, settingsY + 2, SETTING_LABEL.getRGB());

                        // Setting value
                        int valueX = settingsX + mc.fontRendererObj.getStringWidth(settingName) + 12;
                        Color valueColor = setting.isSlider() ? SETTING_VALUE_SLIDER : (setting.getValue().equals("On") ? MODULE_ENABLED : SETTING_VALUE);

                        // Value background
                        int valueWidth = mc.fontRendererObj.getStringWidth(settingValue);
                        drawRect(valueX - 2, settingsY, valueX + valueWidth + 4, settingsY + 11, new Color(30, 30, 35, 255).getRGB());

                        mc.fontRendererObj.drawString(settingValue, valueX, settingsY + 2, valueColor.getRGB());

                        settingsY += 13;
                    }
                }

                moduleY += 13;
            }

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        private List<Module> getModulesInCategory(int tabIndex) {
            List<Module> result = new ArrayList<>();
            Module.Category targetCategory = Module.Category.values()[tabIndex];
            for (Module m : AmongusClient.moduleManager.getModules()) {
                if (m.getCategory() == targetCategory) result.add(m);
            }
            return result;
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            int windowX = (width - 440) / 2;
            int windowY = (height - 280) / 2;

            // Tab click
            int tabX = windowX + 80;
            int tabY = windowY + 4;
            for (int i = 0; i < categoryNames.length; i++) {
                int tabWidth = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 16;
                if (mouseX >= tabX && mouseX <= tabX + tabWidth && mouseY >= tabY && mouseY <= tabY + 14) {
                    activeTab = i;
                    selectedModule = null;
                    return;
                }
                tabX += tabWidth + 3;
            }

            // Module left-click = toggle
            if (mouseButton == 0) {
                List<Module> activeModules = getModulesInCategory(activeTab);
                int moduleX = windowX + 12;
                int moduleY = windowY + 28 - (scrollOffset * 13);

                for (Module m : activeModules) {
                    if (moduleY < windowY + 25) { moduleY += 13; continue; }
                    if (moduleY > windowY + windowHeight - 15) break;

                    if (mouseX >= moduleX && mouseX <= moduleX + 110 && mouseY >= moduleY && mouseY <= moduleY + 12) {
                        m.toggle();
                        return;
                    }
                    moduleY += 13;
                }
            }

            // Module right-click = select for settings
            if (mouseButton == 1) {
                List<Module> activeModules = getModulesInCategory(activeTab);
                int moduleX = windowX + 12;
                int moduleY = windowY + 28 - (scrollOffset * 13);

                for (Module m : activeModules) {
                    if (moduleY < windowY + 25) { moduleY += 13; continue; }
                    if (moduleY > windowY + windowHeight - 15) break;

                    if (mouseX >= moduleX && mouseX <= moduleX + 110 && mouseY >= moduleY && mouseY <= moduleY + 12) {
                        selectedModule = (selectedModule == m) ? null : m;
                        return;
                    }
                    moduleY += 13;
                }
            }

            // Setting value click = cycle or slider adjust
            if (selectedModule != null && mouseButton == 0) {
                List<Module> activeModules = getModulesInCategory(activeTab);
                int moduleIndex = activeModules.indexOf(selectedModule);
                if (moduleIndex >= 0) {
                    int moduleX = windowX + 12;
                    int moduleY = windowY + 28 - (scrollOffset * 13) + (moduleIndex * 13);
                    int settingsX = moduleX + 120;
                    int settingsY = moduleY - (selectedModule.getSettings().size() / 2) * 12;

                    for (Module.Setting setting : selectedModule.getSettings()) {
                        String settingName = setting.getName();
                        int valueX = settingsX + mc.fontRendererObj.getStringWidth(settingName) + 12;
                        String value = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();
                        int valueWidth = mc.fontRendererObj.getStringWidth(value);

                        if (mouseX >= valueX - 2 && mouseX <= valueX + valueWidth + 4 && mouseY >= settingsY && mouseY <= settingsY + 11) {
                            if (!setting.isSlider()) {
                                setting.cycle();
                            } else {
                                double current = setting.getDoubleValue();
                                double next = current + setting.getIncrement();
                                if (next <= setting.getMax()) setting.setValue(String.valueOf(next));
                            }
                            return;
                        }
                        settingsY += 13;
                    }
                }
            }

            // Right-click on slider decreases
            if (selectedModule != null && mouseButton == 1) {
                List<Module> activeModules = getModulesInCategory(activeTab);
                int moduleIndex = activeModules.indexOf(selectedModule);
                if (moduleIndex >= 0) {
                    int moduleX = windowX + 12;
                    int moduleY = windowY + 28 - (scrollOffset * 13) + (moduleIndex * 13);
                    int settingsX = moduleX + 120;
                    int settingsY = moduleY - (selectedModule.getSettings().size() / 2) * 12;

                    for (Module.Setting setting : selectedModule.getSettings()) {
                        String settingName = setting.getName();
                        int valueX = settingsX + mc.fontRendererObj.getStringWidth(settingName) + 12;
                        String value = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();
                        int valueWidth = mc.fontRendererObj.getStringWidth(value);

                        if (mouseX >= valueX - 2 && mouseX <= valueX + valueWidth + 4 && mouseY >= settingsY && mouseY <= settingsY + 11) {
                            if (setting.isSlider()) {
                                double current = setting.getDoubleValue();
                                double next = current - setting.getIncrement();
                                if (next >= setting.getMin()) setting.setValue(String.valueOf(next));
                            }
                            return;
                        }
                        settingsY += 13;
                    }
                }
            }
        }

        @Override
        public boolean doesGuiPauseGame() { return false; }
    }
}
