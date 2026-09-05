package com.stuffaboutmc.client.gui;

import com.stuffaboutmc.client.Client;
import com.stuffaboutmc.client.font.CustomFont;
import com.stuffaboutmc.client.module.Module;
import com.stuffaboutmc.client.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ClickGUI extends GuiScreen {

    public static ClickGUI instance;
    private ArrayList<Module> modules = new ArrayList<>();
    private boolean dragging = false;
    private int dragX, dragY;
    private int guiX = 100, guiY = 50;
    private int guiWidth = 300, guiHeight = 400;
    private int scrollOffset = 0;
    private Module selectedModule = null;
    private boolean showSettings = false;
    private int settingsX, settingsY;
    private int settingsWidth = 200, settingsHeight = 250;

    public ClickGUI() {
        instance = this;
        modules = Client.instance.moduleManager.getModules();
        modules.sort(Comparator.comparing(Module::getName));
        guiX = (Minecraft.getMinecraft().displayWidth / 2) - (guiWidth / 2);
        guiY = (Minecraft.getMinecraft().displayHeight / 2) - (guiHeight / 2);
    }

    @Override
    public void initGui() {
        super.initGui();
        if (CustomFont.TITLE == null) {
            CustomFont.init();
        }
        // CRITICAL: do not reset scroll or selection here
        // that's what was causing the grey rectangle — the list was empty on init
        if (modules.isEmpty()) {
            modules = Client.instance.moduleManager.getModules();
            modules.sort(Comparator.comparing(Module::getName));
        }
        // ensure settings panel closes on reopen
        showSettings = false;
        selectedModule = null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(guiX, guiY, 0);

        // background — semi-transparent dark
        drawRect(0, 0, guiWidth, guiHeight, new Color(20, 20, 20, 220).getRGB());
        drawRect(0, 0, guiWidth, 20, new Color(30, 30, 30, 255).getRGB());

        // title
        if (CustomFont.TITLE != null) {
            CustomFont.TITLE.drawString("VANTA", 8, 4, 0xFFFFFF);
        } else {
            fontRendererObj.drawString("VANTA", 8, 4, 0xFFFFFF);
        }

        // close button
        int closeX = guiWidth - 20;
        drawRect(closeX, 2, closeX + 16, 18, new Color(200, 40, 40, 200).getRGB());
        fontRendererObj.drawString("X", closeX + 4, 4, 0xFFFFFF);

        if (showSettings && selectedModule != null) {
            drawSettingsPanel(mouseX, mouseY, partialTicks);
        } else {
            drawModuleList(mouseX, mouseY, partialTicks);
        }

        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawModuleList(int mouseX, int mouseY, float partialTicks) {
        int yOffset = 24 - scrollOffset;
        int maxY = guiHeight - 10;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int scale = Minecraft.getMinecraft().gameSettings.guiScale;
        int scissorX = guiX * scale;
        int scissorY = (Minecraft.getMinecraft().displayHeight - (guiY + guiHeight)) * scale;
        int scissorW = guiWidth * scale;
        int scissorH = (guiHeight - 24) * scale;
        GL11.glScissor(scissorX, scissorY, scissorW, scissorH);

        for (Module m : modules) {
            if (yOffset < 0 || yOffset > guiHeight - 30) {
                yOffset += 26;
                continue;
            }
            int x = 4;
            int w = guiWidth - 8;
            int h = 22;
            Color bg = m.isEnabled() ? new Color(60, 120, 200, 180) : new Color(40, 40, 40, 180);
            drawRect(x, yOffset, x + w, yOffset + h, bg.getRGB());

            if (CustomFont.TITLE != null) {
                CustomFont.TITLE.drawString(m.getName(), x + 6, yOffset + 4, 0xFFFFFF);
            } else {
                fontRendererObj.drawString(m.getName(), x + 6, yOffset + 4, 0xFFFFFF);
            }

            // hover highlight
            if (mouseX > guiX + x && mouseX < guiX + x + w &&
                mouseY > guiY + yOffset && mouseY < guiY + yOffset + h) {
                drawRect(x, yOffset, x + w, yOffset + h, new Color(255, 255, 255, 40).getRGB());
            }

            yOffset += 26;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void drawSettingsPanel(int mouseX, int mouseY, float partialTicks) {
        // panel on the right of main gui
        int panelX = guiWidth + 4;
        int panelY = 24;
        int pw = settingsWidth;
        int ph = settingsHeight;

        drawRect(panelX, panelY, panelX + pw, panelY + ph, new Color(25, 25, 25, 230).getRGB());
        drawRect(panelX, panelY, panelX + pw, panelY + 20, new Color(40, 40, 40, 255).getRGB());

        if (CustomFont.TITLE != null) {
            CustomFont.TITLE.drawString(selectedModule.getName(), panelX + 6, panelY + 3, 0xFFFFFF);
        } else {
            fontRendererObj.drawString(selectedModule.getName(), panelX + 6, panelY + 3, 0xFFFFFF);
        }

        int y = panelY + 26;
        for (Setting s : selectedModule.getSettings()) {
            if (s.isBoolean()) {
                drawBooleanSetting(s, panelX, y, mouseX, mouseY);
                y += 24;
            } else if (s.isSlider()) {
                drawSliderSetting(s, panelX, y, mouseX, mouseY);
                y += 28;
            } else if (s.isMode()) {
                drawModeSetting(s, panelX, y, mouseX, mouseY);
                y += 24;
            }
        }
    }

    private void drawBooleanSetting(Setting s, int x, int y, int mouseX, int mouseY) {
        String label = s.getName();
        boolean value = s.getBooleanValue();
        drawRect(x + 4, y, x + 150, y + 18, new Color(30, 30, 30, 200).getRGB());
        fontRendererObj.drawString(label, x + 8, y + 4, 0xCCCCCC);
        drawRect(x + 130, y + 3, x + 146, y + 15, value ? new Color(60, 200, 60).getRGB() : new Color(200, 60, 60).getRGB());
    }

    private void drawSliderSetting(Setting s, int x, int y, int mouseX, int mouseY) {
        String label = s.getName() + ": " + String.format("%.1f", s.getValue());
        drawRect(x + 4, y, x + 150, y + 22, new Color(30, 30, 30, 200).getRGB());
        fontRendererObj.drawString(label, x + 8, y + 4, 0xCCCCCC);
        int sliderX = x + 8;
        int sliderY = y + 16;
        int sliderW = 130;
        int sliderH = 4;
        drawRect(sliderX, sliderY, sliderX + sliderW, sliderY + sliderH, new Color(80, 80, 80).getRGB());
        float percent = (float) ((s.getValue() - s.getMin()) / (s.getMax() - s.getMin()));
        int fill = (int) (sliderW * percent);
        drawRect(sliderX, sliderY, sliderX + fill, sliderY + sliderH, new Color(80, 180, 255).getRGB());
    }

    private void drawModeSetting(Setting s, int x, int y, int mouseX, int mouseY) {
        String label = s.getName() + ": " + s.getMode();
        drawRect(x + 4, y, x + 150, y + 18, new Color(30, 30, 30, 200).getRGB());
        fontRendererObj.drawString(label, x + 8, y + 4, 0xCCCCCC);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int relX = mouseX - guiX;
        int relY = mouseY - guiY;

        // close button
        if (relX > guiWidth - 20 && relX < guiWidth - 4 && relY > 2 && relY < 18) {
            close();
            return;
        }

        if (showSettings && selectedModule != null) {
            // handle settings clicks
            int panelX = guiWidth + 4;
            int y = 26;
            for (Setting s : selectedModule.getSettings()) {
                if (s.isBoolean()) {
                    if (mouseX > guiX + panelX + 4 && mouseX < guiX + panelX + 150 &&
                        mouseY > guiY + y && mouseY < guiY + y + 18) {
                        s.toggle();
                        return;
                    }
                    y += 24;
                } else if (s.isSlider()) {
                    if (mouseX > guiX + panelX + 4 && mouseX < guiX + panelX + 150 &&
                        mouseY > guiY + y && mouseY < guiY + y + 22) {
                        // slider click
                        float percent = (float) (mouseX - (guiX + panelX + 8)) / 130f;
                        float val = s.getMin() + percent * (s.getMax() - s.getMin());
                        s.setValue(Math.round(val / s.getInc()) * s.getInc());
                        return;
                    }
                    y += 28;
                } else if (s.isMode()) {
                    if (mouseX > guiX + panelX + 4 && mouseX < guiX + panelX + 150 &&
                        mouseY > guiY + y && mouseY < guiY + y + 18) {
                        s.cycle();
                        return;
                    }
                    y += 24;
                }
            }
            return;
        }

        // module list click
        int yOffset = 24 - scrollOffset;
        for (Module m : modules) {
            if (yOffset < 0 || yOffset > guiHeight - 30) {
                yOffset += 26;
                continue;
            }
            int x = 4;
            int w = guiWidth - 8;
            int h = 22;
            if (relX > x && relX < x + w && relY > yOffset && relY < yOffset + h) {
                if (mouseButton == 0) {
                    m.toggle();
                } else if (mouseButton == 1) {
                    selectedModule = m;
                    showSettings = true;
                    settingsX = guiWidth + 4;
                    settingsY = 24;
                }
                return;
            }
            yOffset += 26;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            scrollOffset -= wheel / 120 * 15;
            int maxScroll = modules.size() * 26 - (guiHeight - 30);
            if (maxScroll < 0) maxScroll = 0;
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) { // ESC
            close();
            return;
        }
        if (keyCode == 0x9) { // TAB
            showSettings = false;
            selectedModule = null;
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    private void close() {
        Minecraft.getMinecraft().displayGuiScreen(null);
        if (Minecraft.getMinecraft().currentScreen == null) {
            Minecraft.getMinecraft().setIngameFocus();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // compat method to toggle from keybind
    public void toggleVisibility() {
        if (Minecraft.getMinecraft().currentScreen == this) {
            close();
        } else {
            Minecraft.getMinecraft().displayGuiScreen(this);
        }
    }
}
