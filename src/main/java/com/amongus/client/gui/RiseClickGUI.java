package com.amongus.client.gui;

import com.amongus.client.ModuleManager;
import com.amongus.client.modules.ClickGUIModule;
import com.amongus.client.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class RiseClickGUI extends GuiScreen {

    // ---------- Get style from ClickGUIModule ----------
    private ClickGUIModule.Style getStyle() {
        return ClickGUIModule.getCurrentStyle();
    }

    private boolean dragging = false;
    private int dragX, dragY;
    private int windowX = 100, windowY = 100;
    private int windowWidth = 400, windowHeight = 300;
    private float scrollOffset = 0;
    private final int cardHeight = 30;
    private Module selectedModule = null;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.enableBlend();

        ClickGUIModule.Style currentStyle = getStyle();
        int w = windowWidth, h = windowHeight;
        int bgColor, borderColor, headerColor, accentColor;
        switch (currentStyle) {
            case RISE:
                bgColor = 0xFF171A21;
                borderColor = 0xFF2A1A1A;
                headerColor = 0xFF1A0A0A;
                accentColor = 0xFFE60000;
                break;
            case VAPE:
                bgColor = 0xFF1A1A1A;
                borderColor = 0xFF2A2A2A;
                headerColor = 0xFF151515;
                accentColor = 0xFF4FC3F7;
                break;
            case AUGUSTUS:
                bgColor = 0xFF0D0A0F;
                borderColor = 0xFF2A1A3A;
                headerColor = 0xFF110D14;
                accentColor = 0xFF9B59B6;
                break;
            case PRESTIGE:
            default:
                bgColor = 0xFF0D0D0D;
                borderColor = 0xFF3A0A0A;
                headerColor = 0xFF0A0A0A;
                accentColor = 0xFFE60000;
                break;
        }

        // Shadow and panel
        drawRect(windowX + 4, windowY + 4, windowX + w + 4, windowY + h + 4, 0x80000000);
        drawRect(windowX, windowY, windowX + w, windowY + h, bgColor);
        drawRect(windowX, windowY, windowX + w, windowY + 1, borderColor);

        // Header – lowercase style name
        drawRect(windowX, windowY, windowX + w, windowY + 22, headerColor);
        mc.fontRendererObj.drawStringWithShadow(
                currentStyle.getDisplayName() + " clickgui",
                windowX + 6, windowY + 7, 0xFFFFFF
        );

        // Module list
        List<Module> sorted = new ArrayList<>(ModuleManager.modules);
        sorted.sort(Comparator.comparing(Module::getName));
        int startY = windowY + 24;
        int maxVisible = (h - 24) / cardHeight;
        int total = sorted.size();
        int scrollMax = Math.max(0, total * cardHeight - (h - 24));

        for (int i = 0; i < sorted.size(); i++) {
            Module mod = sorted.get(i);
            int yPos = startY + i * cardHeight - (int) scrollOffset;
            if (yPos + cardHeight < startY || yPos > windowY + h) continue;

            int cardColor = mod.isEnabled() ? accentColor | 0x66000000 : 0xFF222222;
            if (currentStyle == ClickGUIModule.Style.AUGUSTUS) {
                cardColor = mod.isEnabled() ? 0xAA2A1A3A : 0xFF16101C;
            }
            drawRect(windowX + 2, yPos, windowX + w - 2, yPos + cardHeight, cardColor);

            if (currentStyle == ClickGUIModule.Style.AUGUSTUS && mod.isEnabled()) {
                drawRect(windowX + 2, yPos, windowX + 4, yPos + cardHeight, accentColor);
            }
            if (currentStyle == ClickGUIModule.Style.PRESTIGE && mod.isEnabled()) {
                drawRect(windowX + 2, yPos, windowX + 3, yPos + cardHeight, accentColor);
            }

            int textColor = mod.isEnabled() ? 0xFFFFFF : 0xAAAAAA;
            if (currentStyle == ClickGUIModule.Style.PRESTIGE) textColor = 0xD9D9D9;
            mc.fontRendererObj.drawStringWithShadow(mod.getName(), windowX + 8, yPos + 8, textColor);

            // Keybind badge
            int key = mod.getKeyBind();
            if (key != 0) {
                String keyName = Keyboard.getKeyName(key);
                int kw = mc.fontRendererObj.getStringWidth(keyName);
                drawRect(windowX + w - 50 - kw, yPos + 4, windowX + w - 4, yPos + 22, 0x55000000);
                mc.fontRendererObj.drawStringWithShadow(keyName, windowX + w - 48 - kw, yPos + 8, 0xFFFFFF);
            }

            // Toggle button
            if (currentStyle == ClickGUIModule.Style.VAPE) {
                drawRect(windowX + w - 30, yPos + 8, windowX + w - 20, yPos + 18, mod.isEnabled() ? accentColor : 0xFF555555);
                if (mod.isEnabled()) mc.fontRendererObj.drawStringWithShadow("✓", windowX + w - 27, yPos + 8, 0xFFFFFF);
            } else {
                int toggleX = windowX + w - 40;
                int toggleY = yPos + 6;
                int toggleW = 30, toggleH = 18;
                drawRect(toggleX, toggleY, toggleX + toggleW, toggleY + toggleH, mod.isEnabled() ? accentColor : 0xFF555555);
                int knobX = mod.isEnabled() ? toggleX + toggleW - 14 : toggleX + 2;
                drawRect(knobX, toggleY + 2, knobX + 12, toggleY + toggleH - 2, 0xFFFFFFFF);
            }

            // Expand indicator (right-click to expand)
            if (hasProperties(mod)) {
                mc.fontRendererObj.drawStringWithShadow("▶", windowX + w - 60, yPos + 8, 0x888888);
            }
        }

        // Scrollbar
        if (total > maxVisible) {
            int barHeight = (int) ((float) maxVisible / total * (h - 24));
            int barY = startY + (int) ((float) scrollOffset / scrollMax * (h - 24 - barHeight));
            drawRect(windowX + w - 6, barY, windowX + w - 2, barY + barHeight, 0x66FFFFFF);
        }

        // Expanded settings
        if (selectedModule != null && hasProperties(selectedModule)) {
            drawExpandedEditor(selectedModule, mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    // ---------- Mouse handling ----------
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // Drag header (left-click only)
        if (mouseButton == 0 && mouseX >= windowX && mouseX <= windowX + windowWidth && mouseY >= windowY && mouseY <= windowY + 22) {
            dragging = true;
            dragX = mouseX - windowX;
            dragY = mouseY - windowY;
            return;
        }

        // Module interactions
        List<Module> sorted = new ArrayList<>(ModuleManager.modules);
        sorted.sort(Comparator.comparing(Module::getName));
        for (int i = 0; i < sorted.size(); i++) {
            Module mod = sorted.get(i);
            int yPos = windowY + 24 + i * cardHeight - (int) scrollOffset;
            if (yPos + cardHeight < windowY + 24 || yPos > windowY + windowHeight) continue;

            // Left-click: toggle only on toggle button
            if (mouseButton == 0) {
                int toggleX, toggleW = 30;
                if (getStyle() == ClickGUIModule.Style.VAPE) toggleX = windowX + windowWidth - 30;
                else toggleX = windowX + windowWidth - 40;
                int toggleY = yPos + 6;
                if (mouseX >= toggleX && mouseX <= toggleX + toggleW && mouseY >= toggleY && mouseY <= toggleY + 18) {
                    mod.toggle();
                    return;
                }
            }

            // Right-click: expand/collapse settings (anywhere on card)
            if (mouseButton == 1) {
                if (mouseX >= windowX + 2 && mouseX <= windowX + windowWidth - 2 && mouseY >= yPos && mouseY <= yPos + cardHeight) {
                    if (selectedModule == mod) {
                        selectedModule = null;
                    } else if (hasProperties(mod)) {
                        selectedModule = mod;
                    }
                    return;
                }
            }

            // Middle-click: keybind
            if (mouseButton == 2) {
                if (mouseX >= windowX + 2 && mouseX <= windowX + windowWidth - 2 && mouseY >= yPos && mouseY <= yPos + cardHeight) {
                    mc.displayGuiScreen(new KeybindScreen(mod));
                    return;
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            int totalHeight = ModuleManager.modules.size() * cardHeight;
            int visibleHeight = windowHeight - 24;
            int maxScroll = Math.max(0, totalHeight - visibleHeight);
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - scroll / 8));
        }
    }

    @Override
    public void updateScreen() {
        if (dragging) {
            windowX = Mouse.getX() - dragX;
            windowY = Minecraft.getMinecraft().displayHeight - Mouse.getY() - dragY;
        }
        super.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // ---------- Keybind screen ----------
    private class KeybindScreen extends GuiScreen {
        private final Module module;
        public KeybindScreen(Module mod) { this.module = mod; }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();
            mc.fontRendererObj.drawStringWithShadow("Press any key for " + module.getName(), width / 2 - 100, height / 2 - 10, 0xFFFFFF);
            mc.fontRendererObj.drawStringWithShadow("ESC to cancel", width / 2 - 60, height / 2 + 20, 0xAAAAAA);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                mc.displayGuiScreen(RiseClickGUI.this);
                return;
            }
            module.setKeyBind(keyCode);
            mc.displayGuiScreen(RiseClickGUI.this);
            super.keyTyped(typedChar, keyCode);
        }
    }

    // ---------- Expanded editor ----------
    private void drawExpandedEditor(Module mod, int mouseX, int mouseY) {
        int x = windowX + 20;
        int y = windowY + 60;
        int w = windowWidth - 40;
        int h = 150;
        drawRect(x, y, x + w, y + h, 0xCC000000);
        drawRect(x, y, x + w, y + 1, 0xFFAAAAAA);

        List<Field> props = getProperties(mod);
        if (props.isEmpty()) return;

        int lineHeight = 20;
        int currentY = y + 10;
        for (Field f : props) {
            try {
                f.setAccessible(true);
                Class<?> type = f.getType();
                Object value = f.get(mod);

                mc.fontRendererObj.drawStringWithShadow(f.getName(), x + 6, currentY, 0xFFFFFF);

                if (type == boolean.class || type == Boolean.class) {
                    boolean val = (Boolean) value;
                    drawRect(x + w - 40, currentY, x + w - 20, currentY + 16, val ? 0xFF00AA00 : 0xFF555555);
                    if (val) mc.fontRendererObj.drawStringWithShadow("✓", x + w - 34, currentY + 2, 0xFFFFFF);
                    if (mouseX >= x + w - 40 && mouseX <= x + w - 20 && mouseY >= currentY && mouseY <= currentY + 16) {
                        f.setBoolean(mod, !val);
                    }
                } else if (type.isEnum()) {
                    String valStr = value.toString();
                    mc.fontRendererObj.drawStringWithShadow(valStr, x + w - 100, currentY, 0xCCCCCC);
                    if (mouseX >= x + w - 100 && mouseX <= x + w - 20 && mouseY >= currentY && mouseY <= currentY + 16) {
                        Object[] consts = type.getEnumConstants();
                        int idx = Arrays.asList(consts).indexOf(value);
                        idx = (idx + (Mouse.isButtonDown(1) ? -1 : 1)) % consts.length;
                        if (idx < 0) idx += consts.length;
                        f.set(mod, consts[idx]);
                    }
                } else if (type == float.class || type == Float.class || type == double.class || type == int.class) {
                    float val = ((Number) value).floatValue();
                    int sliderX = x + w - 120;
                    drawRect(sliderX, currentY + 6, sliderX + 100, currentY + 10, 0xFF444444);
                    float ratio = (val - getMin(f, mod)) / (getMax(f, mod) - getMin(f, mod));
                    drawRect(sliderX, currentY + 6, sliderX + (int)(ratio * 100), currentY + 10, 0xFF00AA00);
                    mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", val), sliderX + 105, currentY, 0xCCCCCC);
                    if (mouseX >= sliderX && mouseX <= sliderX + 100 && mouseY >= currentY && mouseY <= currentY + 20) {
                        float newVal = getMin(f, mod) + (mouseX - sliderX) / 100f * (getMax(f, mod) - getMin(f, mod));
                        if (type == int.class) f.setInt(mod, Math.round(newVal));
                        else f.setFloat(mod, newVal);
                    }
                }
                currentY += lineHeight;
            } catch (Exception ignored) {}
        }
    }

    // ---------- Utility ----------
    private boolean hasProperties(Module m) {
        return !getProperties(m).isEmpty();
    }

    private List<Field> getProperties(Module m) {
        List<Field> list = new ArrayList<>();
        for (Field f : m.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Property.class)) {
                list.add(f);
            }
        }
        return list;
    }

    private float getMin(Field f, Object obj) { return 0; }
    private float getMax(Field f, Object obj) { return 10; }

    // ---------- Annotation ----------
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface Property {}
}
