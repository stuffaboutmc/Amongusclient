package com.amongus.client.modules.render;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Module {
    private static final Color WINDOW_BG = new Color(55, 55, 60, 255);
    private static final Color TAB_ACTIVE = new Color(80, 80, 88, 255);
    private static final Color TAB_INACTIVE = new Color(45, 45, 50, 255);
    private static final Color MODULE_ENABLED = new Color(0, 255, 128, 255);
    private static final Color MODULE_DISABLED = new Color(180, 180, 180, 255);
    private static final Color SETTING_LABEL = new Color(150, 150, 155, 255);
    private static final Color SETTING_VALUE = new Color(255, 255, 255, 255);
    private static final Color OUTLINE = new Color(80, 80, 85, 255);
    private static final Color TITLE_TEXT = new Color(255, 255, 255, 255);

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

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            int windowWidth = 420;
            int windowHeight = 260;
            int windowX = (width - windowWidth) / 2;
            int windowY = (height - windowHeight) / 2;

            // Opaque gray background
            drawRect(windowX, windowY, windowX + windowWidth, windowY + windowHeight, WINDOW_BG.getRGB());
            drawRect(windowX, windowY, windowX + windowWidth, windowY + 1, OUTLINE.getRGB());
            drawRect(windowX, windowY + windowHeight - 1, windowX + windowWidth, windowY + windowHeight, OUTLINE.getRGB());
            drawRect(windowX, windowY, windowX + 1, windowY + windowHeight, OUTLINE.getRGB());
            drawRect(windowX + windowWidth - 1, windowY, windowX + windowWidth, windowY + windowHeight, OUTLINE.getRGB());

            // Augustus title
            mc.fontRendererObj.drawStringWithShadow("Augustus", windowX + 7, windowY + 6, TITLE_TEXT.getRGB());

            // Chrome-style tabs
            int tabX = windowX + 70;
            int tabY = windowY + 4;
            for (int i = 0; i < categoryNames.length; i++) {
                String tabName = categoryNames[i];
                int tabWidth = mc.fontRendererObj.getStringWidth(tabName) + 14;
                int tabHeight = 14;

                boolean isActive = i == activeTab;
                drawRect(tabX, tabY, tabX + tabWidth, tabY + tabHeight, isActive ? TAB_ACTIVE.getRGB() : TAB_INACTIVE.getRGB());
                drawRect(tabX, tabY, tabX + tabWidth, tabY + 1, OUTLINE.getRGB());
                drawRect(tabX, tabY, tabX + 1, tabY + tabHeight, OUTLINE.getRGB());
                drawRect(tabX + tabWidth - 1, tabY, tabX + tabWidth, tabY + tabHeight, OUTLINE.getRGB());

                int tabTextX = tabX + (tabWidth - mc.fontRendererObj.getStringWidth(tabName)) / 2;
                mc.fontRendererObj.drawString(tabName, tabTextX, tabY + 4, isActive ? MODULE_ENABLED.getRGB() : MODULE_DISABLED.getRGB());

                if (isActive) {
                    drawRect(tabX, tabY + tabHeight - 1, tabX + tabWidth, tabY + tabHeight, new Color(0, 255, 128, 255).getRGB());
                }

                tabX += tabWidth + 2;
            }

            // Module list for active tab
            List<Module> activeModules = getModulesInCategory(activeTab);
            int moduleX = windowX + 10;
            int moduleY = windowY + 28;

            for (Module m : activeModules) {
                boolean hovered = mouseX >= moduleX && mouseX <= moduleX + 100 && mouseY >= moduleY && mouseY <= moduleY + 12;

                if (hovered || m == selectedModule) {
                    drawRect(moduleX - 3, moduleY, moduleX + 100, moduleY + 12, new Color(70, 70, 76, 255).getRGB());
                }

                Color moduleColor = m.isEnabled() ? MODULE_ENABLED : MODULE_DISABLED;
                mc.fontRendererObj.drawStringWithShadow(m.getName(), moduleX, moduleY + 2, moduleColor.getRGB());

                // Settings for selected module, one per line to the right
                if (m == selectedModule && m.getSettings().size() > 0) {
                    int settingsX = moduleX + 110;
                    int settingsY = moduleY;

                    for (Module.Setting setting : m.getSettings()) {
                        String settingName = setting.getName();
                        String settingValue = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();

                        mc.fontRendererObj.drawString(settingName + ":", settingsX, settingsY + 2, SETTING_LABEL.getRGB());
                        mc.fontRendererObj.drawString(settingValue, settingsX + mc.fontRendererObj.getStringWidth(settingName) + 10, settingsY + 2, SETTING_VALUE.getRGB());

                        settingsY += 12;
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
                if (m.getCategory() == targetCategory) {
                    result.add(m);
                }
            }
            return result;
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            int windowX = (width - 420) / 2;
            int windowY = (height - 260) / 2;

            // Tab clicking
            int tabX = windowX + 70;
            int tabY = windowY + 4;
            for (int i = 0; i < categoryNames.length; i++) {
                int tabWidth = mc.fontRendererObj.getStringWidth(categoryNames[i]) + 14;
                if (mouseX >= tabX && mouseX <= tabX + tabWidth && mouseY >= tabY && mouseY <= tabY + 14) {
                    activeTab = i;
                    selectedModule = null;
                    return;
                }
                tabX += tabWidth + 2;
            }

            // Module clicking
            if (mouseButton == 0) {
                List<Module> activeModules = getModulesInCategory(activeTab);
                int moduleX = windowX + 10;
                int moduleY = windowY + 28;

                for (Module m : activeModules) {
                    if (mouseX >= moduleX && mouseX <= moduleX + 100 && mouseY >= moduleY && mouseY <= moduleY + 12) {
                        m.toggle();
                        return;
                    }
                    moduleY += 13;
                }
            }

            // Right-click selects module for settings
            if (mouseButton == 1) {
                List<Module> activeModules = getModulesInCategory(activeTab);
                int moduleX = windowX + 10;
                int moduleY = windowY + 28;

                for (Module m : activeModules) {
                    if (mouseX >= moduleX && mouseX <= moduleX + 100 && mouseY >= moduleY && mouseY <= moduleY + 12) {
                        selectedModule = (selectedModule == m) ? null : m;
                        return;
                    }
                    moduleY += 13;
                }
            }

            // Click on a setting value cycles it
            if (selectedModule != null && mouseButton == 0) {
                List<Module> activeModules = getModulesInCategory(activeTab);
                int moduleIndex = activeModules.indexOf(selectedModule);
                if (moduleIndex >= 0) {
                    int moduleX = windowX + 10;
                    int moduleY = windowY + 28 + (moduleIndex * 13);
                    int settingsX = moduleX + 110;
                    int settingsY = moduleY;

                    for (Module.Setting setting : selectedModule.getSettings()) {
                        String settingName = setting.getName();
                        int settingNameWidth = mc.fontRendererObj.getStringWidth(settingName);
                        int valueX = settingsX + settingNameWidth + 10;
                        String value = setting.isSlider() ? String.valueOf(setting.getDoubleValue()) : setting.getValue();
                        int valueWidth = mc.fontRendererObj.getStringWidth(value);

                        if (mouseX >= valueX && mouseX <= valueX + valueWidth + 20 && mouseY >= settingsY && mouseY <= settingsY + 12) {
                            if (!setting.isSlider()) {
                                setting.cycle();
                            } else {
                                double current = setting.getDoubleValue();
                                double next = current + setting.getIncrement();
                                if (next <= setting.getMax()) setting.setValue(String.valueOf(next));
                            }
                            return;
                        }
                        settingsY += 12;
                    }
                }
            }
        }

        @Override
        public boolean doesGuiPauseGame() { return false; }
    }
}
