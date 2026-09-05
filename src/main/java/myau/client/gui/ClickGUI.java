package myau.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import myau.client.gui.font.CustomFont;
import myau.client.gui.font.CustomFontRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;

public class ClickGUI extends GuiScreen {

    private static final ClickGUI INSTANCE = new ClickGUI();

    private float guiScale = 1.0f;
    private boolean showSizeSettings = false;
    private boolean firstTimeDone = false;
    private float scaleSliderValue = 1.0f;

    private GuiStyle currentStyle = GuiStyle.Rise;
    private final LinkedHashMap<String, List<Module>> categories = new LinkedHashMap<>();
    private String selectedCategory = "Combat";
    private Module selectedModule = null;
    private boolean listeningForKeybind = false;
    private int scrollOffset = 0;
    private int maxScroll = 0;

    private float openAnimation = 0f;
    private float settingsSlide = 0f;
    private float targetSettingsSlide = 0f;

    private float panelX, panelY, panelW, panelH;
    private float sidebarW, headerH, moduleW, settingsW;
    private float cornerRadius = 10f;

    private static class Module {
        String name;
        boolean enabled;
        int keyBind;
        String category;
        String description;
        Map<String, Boolean> booleanSettings = new LinkedHashMap<>();
        Map<String, Float> floatSettings = new LinkedHashMap<>();

        Module(String name, boolean enabled, int keyBind, String category, String description) {
            this.name = name;
            this.enabled = enabled;
            this.keyBind = keyBind;
            this.category = category;
            this.description = description;
            this.booleanSettings.put("Enabled", enabled);
        }
    }

    private ClickGUI() {
        categories.put("Combat", new ArrayList<Module>());
        categories.put("Movement", new ArrayList<Module>());
        categories.put("Player", new ArrayList<Module>());
        categories.put("Render", new ArrayList<Module>());
        categories.put("Exploit", new ArrayList<Module>());
        categories.put("HUD", new ArrayList<Module>());

        addModule("Combat", "KillAura", true, Keyboard.KEY_R, "Attacks nearby entities");
        addModule("Combat", "Velocity", false, Keyboard.KEY_NONE, "Reduces knockback");
        addModule("Combat", "AutoTotem", false, Keyboard.KEY_NONE, "Equips totems automatically");
        addModule("Combat", "Criticals", false, Keyboard.KEY_NONE, "Always critical hits");
        addModule("Combat", "Reach", false, Keyboard.KEY_NONE, "Extends attack reach");
        addModule("Combat", "AimAssist", false, Keyboard.KEY_NONE, "Assists with aiming");
        addModule("Combat", "AutoArmor", false, Keyboard.KEY_NONE, "Equips best armor");
        addModule("Combat", "SprintReset", false, Keyboard.KEY_NONE, "Sprint reset for combos");

        addModule("Movement", "Speed", false, Keyboard.KEY_V, "Increases movement speed");
        addModule("Movement", "Flight", false, Keyboard.KEY_F, "Allows flying");
        addModule("Movement", "NoSlow", false, Keyboard.KEY_NONE, "No slowdown effects");
        addModule("Movement", "Scaffold", false, Keyboard.KEY_NONE, "Auto bridge building");
        addModule("Movement", "Step", false, Keyboard.KEY_NONE, "Step up blocks");
        addModule("Movement", "Jesus", false, Keyboard.KEY_NONE, "Walk on water");
        addModule("Movement", "Elytra+", false, Keyboard.KEY_NONE, "Enhanced elytra flight");
        addModule("Movement", "FastFall", false, Keyboard.KEY_NONE, "Faster falling");

        addModule("Player", "AutoEat", false, Keyboard.KEY_NONE, "Eats food automatically");
        addModule("Player", "NoFall", false, Keyboard.KEY_NONE, "Cancels fall damage");
        addModule("Player", "AntiHunger", false, Keyboard.KEY_NONE, "Reduces hunger loss");
        addModule("Player", "FastPlace", false, Keyboard.KEY_NONE, "Places blocks faster");
        addModule("Player", "AutoTool", false, Keyboard.KEY_NONE, "Switches to best tool");
        addModule("Player", "ChestStealer", false, Keyboard.KEY_NONE, "Loots chests fast");
        addModule("Player", "InventoryManager", false, Keyboard.KEY_NONE, "Manages inventory");

        addModule("Render", "ESP", false, Keyboard.KEY_NONE, "Highlights entities");
        addModule("Render", "Tracers", false, Keyboard.KEY_NONE, "Lines to entities");
        addModule("Render", "FullBright", false, Keyboard.KEY_NONE, "Maximum brightness");
        addModule("Render", "Nametags", false, Keyboard.KEY_NONE, "Enhanced nametags");
        addModule("Render", "StorageESP", false, Keyboard.KEY_NONE, "Highlights containers");
        addModule("Render", "Chams", false, Keyboard.KEY_NONE, "See through walls");
        addModule("Render", "BlockESP", false, Keyboard.KEY_NONE, "Highlights blocks");
        addModule("Render", "NoRender", false, Keyboard.KEY_NONE, "Disables rendering");

        addModule("Exploit", "PacketFly", false, Keyboard.KEY_NONE, "Packet based flight");
        addModule("Exploit", "Phase", false, Keyboard.KEY_NONE, "Go through blocks");
        addModule("Exploit", "Disabler", false, Keyboard.KEY_NONE, "Disables anticheat");
        addModule("Exploit", "FastBreak", false, Keyboard.KEY_NONE, "Breaks blocks faster");
        addModule("Exploit", "AutoCrystal", false, Keyboard.KEY_NONE, "End crystal aura");
        addModule("Exploit", "Surround", false, Keyboard.KEY_NONE, "Surrounds with obsidian");
        addModule("Exploit", "HoleFill", false, Keyboard.KEY_NONE, "Fills holes");
        addModule("Exploit", "BedBomb", false, Keyboard.KEY_NONE, "Bed explosion aura");

        addModule("HUD", "ArrayList", true, Keyboard.KEY_NONE, "Shows module list");
        addModule("HUD", "Watermark", true, Keyboard.KEY_NONE, "Client watermark");
        addModule("HUD", "Coordinates", false, Keyboard.KEY_NONE, "Shows coordinates");
        addModule("HUD", "CPS", false, Keyboard.KEY_NONE, "Clicks per second");
        addModule("HUD", "FPS", false, Keyboard.KEY_NONE, "Frames per second");
        addModule("HUD", "Ping", false, Keyboard.KEY_NONE, "Server ping");
        addModule("HUD", "Memory", false, Keyboard.KEY_NONE, "Memory usage");
        addModule("HUD", "Keystrokes", false, Keyboard.KEY_NONE, "Shows key presses");
    }

    private void addModule(String category, String name, boolean enabled, int keyBind, String description) {
        Module m = new Module(name, enabled, keyBind, category, description);
        categories.get(category).add(m);
    }

    public static ClickGUI getInstance() { return INSTANCE; }

    public float getGuiScale() { return guiScale; }
    public void setGuiScale(float s) { guiScale = Math.max(0.5f, Math.min(1.5f, s)); scaleSliderValue = guiScale; }
    public boolean isFirstTimeDone() { return firstTimeDone; }
    public void setFirstTimeDone(boolean v) { firstTimeDone = v; }
    public GuiStyle getStyle() { return currentStyle; }
    public void setStyle(GuiStyle style) { this.currentStyle = style; }

    @Override
    public void initGui() {
        super.initGui();
        if (CustomFont.TITLE == null) {
            CustomFont.init();
        }
        openAnimation = 0f;
        if (!firstTimeDone) {
            showSizeSettings = true;
            scaleSliderValue = guiScale;
        } else {
            showSizeSettings = false;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.displayWidth <= 0 || mc.displayHeight <= 0) return;
        if (mc.currentScreen != this) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();

        openAnimation = Math.min(1f, openAnimation + partialTicks * 0.15f);
        float eased = easeOutCubic(openAnimation);

        float slideTarget = (selectedModule != null && !showSizeSettings) ? 1f : 0f;
        settingsSlide += (slideTarget - settingsSlide) * Math.min(1f, partialTicks * 0.2f);
        if (Math.abs(settingsSlide - slideTarget) < 0.01f) settingsSlide = slideTarget;

        calculateLayout(sw, sh);

        ClickGuiRenderer.drawRect(0, 0, sw, sh, (int) (180 * eased) << 24);

        GlStateManager.pushMatrix();

        if (showSizeSettings) {
            drawSizeSettingsPanel(sw, sh, mouseX, mouseY, eased);
        } else {
            drawClickGUI(sw, sh, mouseX, mouseY, partialTicks, eased);
        }

        GlStateManager.popMatrix();
    }

    private void calculateLayout(int sw, int sh) {
        panelW = Math.min(680, sw * 0.82f) * guiScale;
        panelH = Math.min(420, sh * 0.75f) * guiScale;
        panelX = (sw - panelW) / 2f;
        panelY = (sh - panelH) / 2f;

        headerH = 38 * guiScale;
        sidebarW = 80 * guiScale;
        settingsW = 180 * guiScale;
        moduleW = panelW - sidebarW - settingsW * settingsSlide - 12 * guiScale;
        cornerRadius = 10 * guiScale;
    }

    private void drawClickGUI(int sw, int sh, int mouseX, int mouseY, float pt, float eased) {
        float scale = eased;

        GlStateManager.pushMatrix();
        float cx = panelX + panelW / 2f;
        float cy = panelY + panelH / 2f;
        GlStateManager.translate(cx, cy, 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-cx, -cy, 0);

        int accent = currentStyle.accentColor;
        int bg = currentStyle.bgColor;

        ClickGuiRenderer.drawRoundedRectWithShadow(panelX, panelY, panelW, panelH, cornerRadius,
                withAlpha(bg, 230), withAlpha(0x000000, 80), (int) (6 * guiScale));

        ClickGuiRenderer.drawRoundedRect(panelX, panelY, panelW, headerH, cornerRadius, withAlpha(bg, 240));
        ClickGuiRenderer.drawRect((int) panelX, (int) (panelY + headerH - cornerRadius), (int) (panelX + panelW), (int) (panelY + headerH), withAlpha(bg, 240));

        if (CustomFont.TITLE != null) {
            CustomFont.TITLE.drawString("AMONG US CLIENT", panelX + 14, panelY + (headerH - CustomFont.TITLE.getHeight()) / 2f, 0xFFFFFFFF, true);
        }

        float styleX = panelX + panelW - 10;
        for (int i = GuiStyle.values().length - 1; i >= 0; i--) {
            GuiStyle s = GuiStyle.values()[i];
            float tw = ClickGuiRenderer.getStringWidth(CustomFont.SMALL, s.displayName) + 12 * guiScale;
            styleX -= tw + 4 * guiScale;
            boolean hovered = mouseX >= styleX && mouseX <= styleX + tw && mouseY >= panelY + 6 && mouseY <= panelY + headerH - 6;
            int styleBg = (s == currentStyle) ? s.accentColor : (hovered ? lighten(s.accentColor, 1.3f) : withAlpha(0x333333, 180));
            ClickGuiRenderer.drawRoundedRect(styleX, panelY + 6, tw, headerH - 12, 5 * guiScale, styleBg);
            if (CustomFont.SMALL != null) {
                CustomFont.SMALL.drawString(s.displayName, styleX + 6, panelY + 6 + (headerH - 12 - CustomFont.SMALL.getHeight()) / 2f, 0xFFFFFFFF, false);
            }
        }

        float gearX = styleX - 24 * guiScale;
        boolean gearHovered = mouseX >= gearX && mouseX <= gearX + 20 * guiScale && mouseY >= panelY + 6 && mouseY <= panelY + headerH - 6;
        ClickGuiRenderer.drawRoundedRect(gearX, panelY + 6, 20 * guiScale, headerH - 12, 5 * guiScale, gearHovered ? withAlpha(accent, 120) : withAlpha(0x333333, 180));
        if (CustomFont.BODY != null) {
            CustomFont.BODY.drawString("\u2699", gearX + 4, panelY + 6 + (headerH - 12 - CustomFont.BODY.getHeight()) / 2f, gearHovered ? 0xFFFFFFFF : 0xFFAAAAAA, false);
        }

        float catX = panelX + 6 * guiScale;
        float catY = panelY + headerH + 6 * guiScale;
        float catW = sidebarW - 6 * guiScale;
        float catH = panelH - headerH - 12 * guiScale;
        ClickGuiRenderer.drawRoundedRect(catX, catY, catW, catH, cornerRadius - 2, withAlpha(darken(bg, 0.7f), 220));

        float catItemY = catY + 8 * guiScale;
        float catItemH = 26 * guiScale;
        for (String cat : categories.keySet()) {
            boolean selected = cat.equals(selectedCategory);
            boolean hovered = mouseX >= catX && mouseX <= catX + catW && mouseY >= catItemY && mouseY <= catItemY + catItemH;

            if (selected) {
                ClickGuiRenderer.drawRoundedRect(catX + 3, catItemY, catW - 6, catItemH, 6 * guiScale, withAlpha(accent, 80));
            } else if (hovered) {
                ClickGuiRenderer.drawRoundedRect(catX + 3, catItemY, catW - 6, catItemH, 6 * guiScale, 0x15FFFFFF);
            }

            CustomFontRenderer font = selected ? CustomFont.HEADER : CustomFont.BODY;
            int textCol = selected ? 0xFFFFFFFF : 0xFF999999;
            if (font != null) {
                font.drawString(cat, catX + 10, catItemY + (catItemH - font.getHeight()) / 2f, textCol, false);
                String count = String.valueOf(categories.get(cat).size());
                font.drawString(count, catX + catW - 8 - font.getStringWidth(count), catItemY + (catItemH - font.getHeight()) / 2f, selected ? accent : 0xFF666666, false);
            }
            catItemY += catItemH + 2 * guiScale;
        }

        float modX = panelX + sidebarW + 2 * guiScale;
        float modY = panelY + headerH + 6 * guiScale;
        float modPanelW = moduleW;
        float modH = panelH - headerH - 12 * guiScale;
        ClickGuiRenderer.drawRoundedRect(modX, modY, modPanelW, modH, cornerRadius - 2, withAlpha(darken(bg, 0.85f), 220));

        if (CustomFont.HEADER != null) {
            CustomFont.HEADER.drawString(selectedCategory.toUpperCase(), modX + 10, modY + 8, accent, false);
        }

        List<Module> modules = categories.get(selectedCategory);
        float cardH = 38 * guiScale;
        float cardPad = 4 * guiScale;
        float contentY = modY + 24 * guiScale;
        float contentH = modH - 28 * guiScale;
        maxScroll = Math.max(0, (int) (modules.size() * (cardH + cardPad) - contentH));

        int visibleStart = Math.max(0, (int) (scrollOffset / (cardH + cardPad)));
        int visibleEnd = Math.min(modules.size(), visibleStart + (int) (contentH / (cardH + cardPad)) + 2);

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float scaleFactor = sr.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) ((modX + 4) * scaleFactor), (int) ((Minecraft.getMinecraft().displayHeight / sr.getScaleFactor() - (contentY + contentH)) * scaleFactor),
                (int) ((modPanelW - 8) * scaleFactor), (int) (contentH * scaleFactor));

        for (int i = visibleStart; i < visibleEnd; i++) {
            Module mod = modules.get(i);
            float cardY = contentY + i * (cardH + cardPad) - scrollOffset;
            if (cardY + cardH < contentY || cardY > contentY + contentH) continue;

            boolean cardHovered = mouseX >= modX + 4 && mouseX <= modX + modPanelW - 4
                    && mouseY >= cardY && mouseY <= cardY + cardH;
            int cardBg = cardHovered ? lighten(bg, 1.2f) : bg;
            if (selectedModule == mod) cardBg = lighten(bg, 1.4f);

            ClickGuiRenderer.drawRoundedRect(modX + 4, cardY, modPanelW - 8, cardH, 6 * guiScale, withAlpha(cardBg, 200));

            if (selectedModule == mod) {
                ClickGuiRenderer.drawRoundedRect(modX + 4, cardY, 3, cardH, 1.5f, accent);
            }

            int nameCol = mod.enabled ? 0xFFFFFFFF : 0xFF777777;
            if (CustomFont.BODY != null) {
                CustomFont.BODY.drawString(mod.name, modX + 14, cardY + 6, nameCol, false);
            }

            if (mod.description != null && CustomFont.SMALL != null && CustomFont.BODY != null) {
                CustomFont.SMALL.drawString(mod.description, modX + 14, cardY + 6 + CustomFont.BODY.getHeight() + 2, 0xFF666666, false);
            }

            float toggleW = 36 * guiScale;
            float toggleH = 16 * guiScale;
            float toggleX = modX + modPanelW - toggleW - 10 * guiScale;
            float toggleY = cardY + (cardH - toggleH) / 2f;
            int toggleBg = mod.enabled ? accent : 0xFF333333;
            ClickGuiRenderer.drawRoundedRect(toggleX, toggleY, toggleW, toggleH, toggleH / 2f, toggleBg);
            float knobX = mod.enabled ? toggleX + toggleW - toggleH + 2 : toggleX + 2;
            ClickGuiRenderer.drawRoundedRect(knobX, toggleY + 2, toggleH - 4, toggleH - 4, (toggleH - 4) / 2f, 0xFFDDDDDD);

            String kbText = mod.keyBind != Keyboard.KEY_NONE ? Keyboard.getKeyName(mod.keyBind) : "---";
            float kbW = 30 * guiScale;
            float kbX = toggleX - kbW - 6 * guiScale;
            float kbY = cardY + (cardH - 18 * guiScale) / 2f;
            boolean kbHovered = mouseX >= kbX && mouseX <= kbX + kbW && mouseY >= kbY && mouseY <= kbY + 18 * guiScale;
            ClickGuiRenderer.drawRoundedRect(kbX, kbY, kbW, 18 * guiScale, 4 * guiScale, kbHovered ? withAlpha(accent, 100) : 0xFF2A2A2A);
            if (CustomFont.SMALL != null) {
                CustomFont.SMALL.drawString(kbText, kbX + (kbW - CustomFont.SMALL.getStringWidth(kbText)) / 2f, kbY + (18 * guiScale - CustomFont.SMALL.getHeight()) / 2f, 0xFFAAAAAA, false);
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if (settingsSlide > 0.01f && selectedModule != null) {
            float setW = settingsW;
            float setX = modX + modPanelW + 6 * guiScale;
            float setY = modY;
            float setH = modH;
            float slideOffset = (1f - settingsSlide) * setW;

            GlStateManager.pushMatrix();
            GlStateManager.translate(-slideOffset, 0, 0);

            ClickGuiRenderer.drawRoundedRect(setX, setY, setW, setH, cornerRadius - 2, withAlpha(darken(bg, 0.8f), 220));

            if (CustomFont.HEADER != null) {
                CustomFont.HEADER.drawString("SETTINGS", setX + 10, setY + 8, accent, false);
            }
            if (CustomFont.BODY != null) {
                CustomFont.BODY.drawString(selectedModule.name, setX + 10, setY + 24, 0xFFFFFFFF, false);
            }

            float settingY = setY + 40 * guiScale;
            float settingSpacing = 24 * guiScale;
            float settingPad = 6 * guiScale;

            drawSettingRow(setX + settingPad, settingY, setW - settingPad * 2, "Keybind",
                    selectedModule.keyBind != Keyboard.KEY_NONE ? Keyboard.getKeyName(selectedModule.keyBind) : "None",
                    mouseX, mouseY, accent);
            settingY += settingSpacing;

            for (Map.Entry<String, Boolean> entry : selectedModule.booleanSettings.entrySet()) {
                float tW = 32 * guiScale;
                float tH = 14 * guiScale;
                float rowX = setX + settingPad;
                float rowW = setW - settingPad * 2;

                ClickGuiRenderer.drawRoundedRect(rowX, settingY, rowW, 20 * guiScale, 4 * guiScale, 0xFF222222);
                if (CustomFont.SMALL != null) {
                    CustomFont.SMALL.drawString(entry.getKey(), rowX + 8, settingY + (20 * guiScale - CustomFont.SMALL.getHeight()) / 2f, 0xFFAAAAAA, false);
                }

                float tX = rowX + rowW - tW - 8;
                float tY = settingY + (20 * guiScale - tH) / 2f;
                int tBg = entry.getValue() ? accent : 0xFF333333;
                ClickGuiRenderer.drawRoundedRect(tX, tY, tW, tH, tH / 2f, tBg);
                float kX = entry.getValue() ? tX + tW - tH + 2 : tX + 2;
                ClickGuiRenderer.drawRoundedRect(kX, tY + 2, tH - 4, tH - 4, (tH - 4) / 2f, 0xFFDDDDDD);

                settingY += settingSpacing;
            }

            for (Map.Entry<String, Float> entry : selectedModule.floatSettings.entrySet()) {
                float rowX = setX + settingPad;
                float rowW = setW - settingPad * 2;

                ClickGuiRenderer.drawRoundedRect(rowX, settingY, rowW, 26 * guiScale, 4 * guiScale, 0xFF222222);
                if (CustomFont.SMALL != null) {
                    CustomFont.SMALL.drawString(entry.getKey(), rowX + 8, settingY + 4, 0xFFAAAAAA, false);
                    String valStr = String.format("%.1f", entry.getValue());
                    CustomFont.SMALL.drawString(valStr, rowX + rowW - 8 - CustomFont.SMALL.getStringWidth(valStr), settingY + 4, 0xFFDDDDDD, false);
                }

                float barX = rowX + 8;
                float barW = rowW - 16;
                float barY = settingY + 16 * guiScale;
                float barH = 4 * guiScale;
                ClickGuiRenderer.drawRoundedRect(barX, barY, barW, barH, barH / 2f, 0xFF333333);
                float filled = barW * Math.min(1f, Math.max(0f, entry.getValue() / 2f));
                ClickGuiRenderer.drawRoundedRect(barX, barY, filled, barH, barH / 2f, accent);

                settingY += settingSpacing + 4 * guiScale;
            }

            GlStateManager.popMatrix();
        }

        if (listeningForKeybind && selectedModule != null) {
            ClickGuiRenderer.drawRoundedRect(panelX + panelW / 2f - 80, panelY + panelH - 36, 160, 28, 8, withAlpha(bg, 220));
            if (CustomFont.BODY != null) {
                CustomFont.BODY.drawString("Press a key...", panelX + panelW / 2f - 30, panelY + panelH - 30, accent, true);
            }
        }

        GlStateManager.popMatrix();
    }

    private void drawSettingRow(float x, float y, float w, String label, String value, int mouseX, int mouseY, int accent) {
        ClickGuiRenderer.drawRoundedRect(x, y, w, 20 * guiScale, 4 * guiScale, 0xFF222222);
        if (CustomFont.SMALL != null) {
            CustomFont.SMALL.drawString(label, x + 8, y + (20 * guiScale - CustomFont.SMALL.getHeight()) / 2f, 0xFFAAAAAA, false);
        }

        float btnW = 40 * guiScale;
        float btnX = x + w - btnW - 6;
        float btnY = y + 3;
        float btnH = 14 * guiScale;
        boolean hovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
        ClickGuiRenderer.drawRoundedRect(btnX, btnY, btnW, btnH, 4 * guiScale, hovered ? withAlpha(accent, 120) : 0xFF333333);
        if (CustomFont.SMALL != null) {
            CustomFont.SMALL.drawString(value, btnX + (btnW - CustomFont.SMALL.getStringWidth(value)) / 2f, btnY + (btnH - CustomFont.SMALL.getHeight()) / 2f, 0xFFDDDDDD, false);
        }
    }

    private void drawSizeSettingsPanel(int sw, int sh, int mouseX, int mouseY, float eased) {
        float w = 340 * guiScale;
        float h = 220 * guiScale;
        float x = (sw - w) / 2f;
        float y = (sh - h) / 2f;

        int accent = currentStyle.accentColor;
        int bg = currentStyle.bgColor;

        GlStateManager.pushMatrix();
        float cx = x + w / 2f;
        float cy = y + h / 2f;
        GlStateManager.translate(cx, cy, 0);
        GlStateManager.scale(eased, eased, 1);
        GlStateManager.translate(-cx, -cy, 0);

        ClickGuiRenderer.drawRoundedRectWithShadow(x, y, w, h, cornerRadius, withAlpha(bg, 240), withAlpha(0x000000, 100), (int) (8 * guiScale));

        ScaledResolution clipSr = new ScaledResolution(Minecraft.getMinecraft());
        float clipScale = clipSr.getScaleFactor();
        int clipX = (int) ((x + 2) * clipScale);
        int clipY = (int) ((sh / clipSr.getScaleFactor() - (y + h - 2)) * clipScale);
        int clipW = (int) ((w - 4) * clipScale);
        int clipH = (int) ((h - 4) * clipScale);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(clipX, clipY, clipW, clipH);

        if (CustomFont.TITLE != null) {
            CustomFont.TITLE.drawString("GUI Settings", x + 20, y + 16, 0xFFFFFFFF, true);
        }

        if (CustomFont.BODY != null) {
            CustomFont.BODY.drawString("GUI Scale", x + 20, y + 52, 0xFFAAAAAA, false);
            String scaleText = String.format("%.2fx", scaleSliderValue);
            CustomFont.BODY.drawString(scaleText, x + w - 20 - CustomFont.BODY.getStringWidth(scaleText), y + 52, 0xFFFFFFFF, false);
        }

        float sliderX = x + 20;
        float sliderW = w - 40;
        float sliderY = y + 72;
        float sliderH = 6 * guiScale;
        ClickGuiRenderer.drawRoundedRect(sliderX, sliderY, sliderW, sliderH, sliderH / 2f, 0xFF333333);
        float sliderFill = (scaleSliderValue - 0.5f) / 1.0f;
        ClickGuiRenderer.drawRoundedRect(sliderX, sliderY, sliderW * sliderFill, sliderH, sliderH / 2f, accent);
        float knobX = sliderX + sliderW * sliderFill - 6 * guiScale;
        float knobY = sliderY - 4 * guiScale;
        ClickGuiRenderer.drawRoundedRect(knobX, knobY, 12 * guiScale, sliderH + 8 * guiScale, (sliderH + 8 * guiScale) / 2f, 0xFFDDDDDD);

        float previewScale = scaleSliderValue;
        float pw = 280 * previewScale;
        float availH = h - 130 * guiScale;
        float ph = Math.min(140 * previewScale, availH);
        float px = x + (w - pw) / 2f;
        float py = y + 96;
        ClickGuiRenderer.drawRoundedRect(px, py, pw, ph, 6 * previewScale, withAlpha(darken(bg, 0.9f), 150));
        ClickGuiRenderer.drawRoundedRect(px + 3, py + 3, 30 * previewScale, ph - 6, 4 * previewScale, withAlpha(darken(bg, 0.7f), 200));
        for (int i = 0; i < 3; i++) {
            float rowY = py + 8 + i * (22 * previewScale + 3);
            float rowH = 22 * previewScale;
            if (rowY + rowH > py + ph - 4) break;
            ClickGuiRenderer.drawRoundedRect(px + 36 * previewScale, rowY, pw - 44 * previewScale, rowH, 4 * previewScale, withAlpha(bg, 180));
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        float btnW = 100 * guiScale;
        float btnH = 28 * guiScale;
        float btnX = x + w - btnW - 20;
        float btnY = y + h - btnH - 16;
        boolean btnHovered = mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH;
        ClickGuiRenderer.drawRoundedRect(btnX, btnY, btnW, btnH, 6 * guiScale, btnHovered ? lighten(accent, 1.2f) : accent);
        if (CustomFont.BODY != null) {
            CustomFont.BODY.drawString("Done", btnX + (btnW - CustomFont.BODY.getStringWidth("Done")) / 2f, btnY + (btnH - CustomFont.BODY.getHeight()) / 2f, 0xFFFFFFFF, false);
        }

        float resetW = 80 * guiScale;
        float resetX = btnX - resetW - 10;
        boolean resetHovered = mouseX >= resetX && mouseX <= resetX + resetW && mouseY >= btnY && mouseY <= btnY + btnH;
        ClickGuiRenderer.drawRoundedRect(resetX, btnY, resetW, btnH, 6 * guiScale, resetHovered ? 0xFF555555 : 0xFF333333);
        if (CustomFont.BODY != null) {
            CustomFont.BODY.drawString("Reset", resetX + (resetW - CustomFont.BODY.getStringWidth("Reset")) / 2f, btnY + (btnH - CustomFont.BODY.getHeight()) / 2f, 0xFFAAAAAA, false);
        }

        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton != 0) return;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        calculateLayout(sw, sh);

        if (showSizeSettings) {
            handleSizeSettingsClick(mouseX, mouseY, sw, sh);
            return;
        }

        int accent = currentStyle.accentColor;

        float gearX = panelX + panelW - 10;
        for (int i = GuiStyle.values().length - 1; i >= 0; i--) {
            GuiStyle s = GuiStyle.values()[i];
            float tw = ClickGuiRenderer.getStringWidth(CustomFont.SMALL, s.displayName) + 12 * guiScale;
            gearX -= tw + 4 * guiScale;
        }
        gearX -= 24 * guiScale;
        if (mouseX >= gearX && mouseX <= gearX + 20 * guiScale && mouseY >= panelY + 6 && mouseY <= panelY + headerH - 6) {
            showSizeSettings = true;
            scaleSliderValue = guiScale;
            playClickSound();
            return;
        }

        float styleX = panelX + panelW - 10;
        for (int i = GuiStyle.values().length - 1; i >= 0; i--) {
            GuiStyle s = GuiStyle.values()[i];
            float tw = ClickGuiRenderer.getStringWidth(CustomFont.SMALL, s.displayName) + 12 * guiScale;
            styleX -= tw + 4 * guiScale;
            if (mouseX >= styleX && mouseX <= styleX + tw && mouseY >= panelY + 6 && mouseY <= panelY + headerH - 6) {
                currentStyle = s;
                playClickSound();
                return;
            }
        }

        float catX = panelX + 6 * guiScale;
        float catY = panelY + headerH + 6 * guiScale;
        float catW = sidebarW - 6 * guiScale;
        float catItemY = catY + 8 * guiScale;
        float catItemH = 26 * guiScale;
        for (String cat : categories.keySet()) {
            if (mouseX >= catX && mouseX <= catX + catW && mouseY >= catItemY && mouseY <= catItemY + catItemH) {
                selectedCategory = cat;
                scrollOffset = 0;
                selectedModule = null;
                playClickSound();
                return;
            }
            catItemY += catItemH + 2 * guiScale;
        }

        float modX = panelX + sidebarW + 2 * guiScale;
        float modY = panelY + headerH + 6 * guiScale;
        float modPanelW = moduleW;
        float cardH = 38 * guiScale;
        float cardPad = 4 * guiScale;
        float contentY = modY + 24 * guiScale;

        List<Module> modules = categories.get(selectedCategory);
        for (int i = 0; i < modules.size(); i++) {
            Module mod = modules.get(i);
            float cardY = contentY + i * (cardH + cardPad) - scrollOffset;

            if (mouseX >= modX + 4 && mouseX <= modX + modPanelW - 4
                    && mouseY >= cardY && mouseY <= cardY + cardH) {

                float toggleW = 36 * guiScale;
                float toggleH = 16 * guiScale;
                float toggleX = modX + modPanelW - toggleW - 10 * guiScale;
                float toggleY = cardY + (cardH - toggleH) / 2f;
                if (mouseX >= toggleX && mouseX <= toggleX + toggleW && mouseY >= toggleY && mouseY <= toggleY + toggleH) {
                    mod.enabled = !mod.enabled;
                    mod.booleanSettings.put("Enabled", mod.enabled);
                    playClickSound();
                    return;
                }

                float kbW = 30 * guiScale;
                float kbX = toggleX - kbW - 6 * guiScale;
                float kbY = cardY + (cardH - 18 * guiScale) / 2f;
                if (mouseX >= kbX && mouseX <= kbX + kbW && mouseY >= kbY && mouseY <= kbY + 18 * guiScale) {
                    selectedModule = mod;
                    listeningForKeybind = true;
                    return;
                }

                selectedModule = mod;
                playClickSound();
                return;
            }
        }

        if (settingsSlide > 0.5f && selectedModule != null) {
            float setW = settingsW;
            float setX = modX + modPanelW + 6 * guiScale;
            float settingPad = 6 * guiScale;
            float settingSpacing = 24 * guiScale;
            float settingY = modY + 40 * guiScale;

            settingY += settingSpacing;

            for (Map.Entry<String, Boolean> entry : selectedModule.booleanSettings.entrySet()) {
                float rowX = setX + settingPad;
                float rowW = setW - settingPad * 2;
                float tW = 32 * guiScale;
                float tH = 14 * guiScale;
                float tX = rowX + rowW - tW - 8;
                float tY = settingY + (20 * guiScale - tH) / 2f;
                if (mouseX >= tX && mouseX <= tX + tW && mouseY >= tY && mouseY <= tY + tH) {
                    entry.setValue(!entry.getValue());
                    if (entry.getKey().equals("Enabled")) {
                        selectedModule.enabled = entry.getValue();
                    }
                    playClickSound();
                    return;
                }
                settingY += settingSpacing;
            }

            for (Map.Entry<String, Float> entry : selectedModule.floatSettings.entrySet()) {
                float rowX = setX + settingPad;
                float rowW = setW - settingPad * 2;
                float barX = rowX + 8;
                float barW = rowW - 16;
                float barY = settingY + 16 * guiScale;
                if (mouseX >= barX && mouseX <= barX + barW && mouseY >= barY && mouseY <= barY + 6 * guiScale) {
                    float ratio = (float) (mouseX - barX) / barW;
                    entry.setValue(Math.max(0f, Math.min(2f, ratio * 2f)));
                    return;
                }
                settingY += settingSpacing + 4 * guiScale;
            }
        }
    }

    private void handleSizeSettingsClick(int mouseX, int mouseY, int sw, int sh) {
        float w = 340 * guiScale;
        float h = 220 * guiScale;
        float x = (sw - w) / 2f;
        float y = (sh - h) / 2f;
        int accent = currentStyle.accentColor;

        float sliderX = x + 20;
        float sliderW = w - 40;
        float sliderY = y + 72;
        float sliderH = 6 * guiScale;
        if (mouseX >= sliderX && mouseX <= sliderX + sliderW && mouseY >= sliderY - 6 && mouseY <= sliderY + sliderH + 6) {
            float ratio = (float) (mouseX - sliderX) / sliderW;
            scaleSliderValue = Math.max(0.5f, Math.min(1.5f, 0.5f + ratio));
            guiScale = scaleSliderValue;
            playClickSound();
            return;
        }

        float btnW = 100 * guiScale;
        float btnH = 28 * guiScale;
        float btnX = x + w - btnW - 20;
        float btnY = y + h - btnH - 16;
        if (mouseX >= btnX && mouseX <= btnX + btnW && mouseY >= btnY && mouseY <= btnY + btnH) {
            firstTimeDone = true;
            showSizeSettings = false;
            guiScale = scaleSliderValue;
            playClickSound();
            try {
                myau.client.config.ConfigManager.save("default");
            } catch (Exception e) {
            }
            return;
        }

        float resetW = 80 * guiScale;
        float resetX = btnX - resetW - 10;
        if (mouseX >= resetX && mouseX <= resetX + resetW && mouseY >= btnY && mouseY <= btnY + btnH) {
            scaleSliderValue = 1.0f;
            guiScale = 1.0f;
            playClickSound();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (listeningForKeybind && selectedModule != null) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE) {
                selectedModule.keyBind = Keyboard.KEY_NONE;
            } else {
                selectedModule.keyBind = keyCode;
            }
            listeningForKeybind = false;
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            if (showSizeSettings) {
                firstTimeDone = true;
                showSizeSettings = false;
                try {
                    myau.client.config.ConfigManager.save("default");
                } catch (Exception e) {
                }
            }
            mc.displayGuiScreen(null);
            return;
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            scrollOffset -= dWheel > 0 ? 18 : -18;
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void playClickSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(
                net.minecraft.client.audio.PositionedSoundRecord.create(
                        new net.minecraft.util.ResourceLocation("random.button")));
    }

    private int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    private int darken(int color, float factor) {
        int a = (color >> 24 & 0xFF);
        int r = (int) (((color >> 16 & 0xFF)) * factor);
        int g = (int) (((color >> 8 & 0xFF)) * factor);
        int b = (int) ((color & 0xFF) * factor);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private int lighten(int color, float factor) {
        int a = (color >> 24 & 0xFF);
        int r = Math.min(255, (int) (((color >> 16 & 0xFF)) * factor));
        int g = Math.min(255, (int) (((color >> 8 & 0xFF)) * factor));
        int b = Math.min(255, (int) ((color & 0xFF) * factor));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private float easeOutCubic(float t) {
        return 1f - (float) Math.pow(1f - t, 3);
    }

    public void onKeyPress(int keyCode) {
        for (List<Module> mods : categories.values()) {
            for (Module mod : mods) {
                if (mod.keyBind == keyCode && mod.keyBind != Keyboard.KEY_NONE) {
                    mod.enabled = !mod.enabled;
                    mod.booleanSettings.put("Enabled", mod.enabled);
                }
            }
        }
    }

    public List<String> getEnabledModules() {
        List<String> list = new ArrayList<String>();
        for (List<Module> mods : categories.values()) {
            for (Module mod : mods) {
                if (mod.enabled) list.add(mod.name);
            }
        }
        return list;
    }

    public boolean isModuleEnabled(String name) {
        for (List<Module> mods : categories.values()) {
            for (Module mod : mods) {
                if (mod.name.equals(name)) return mod.enabled;
            }
        }
        return false;
    }
}
