#!/bin/bash

# Delete conflicting folders
rm -rf src/main/java/com/stuffaboutmc
rm -rf src/main/java/myau/client/modules
rm -rf src/main/java/myau/client/gui/font/font

# Create correct folders
mkdir -p src/main/java/myau/client/module/impl
mkdir -p src/main/java/myau/client/settings
mkdir -p src/main/java/myau/client/core
mkdir -p src/main/java/myau/client/gui
mkdir -p src/main/java/myau/client/font

# Write Module.java
cat > src/main/java/myau/client/module/Module.java << 'EOF'
package myau.client.module;

import myau.client.settings.Setting;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    private String name, category;
    private int keybind;
    private boolean enabled;
    private List<Setting> settings = new ArrayList<>();
    public boolean wasPressed = false;

    public Module(String name, String category) {
        this.name = name;
        this.category = category;
        this.keybind = 0;
        this.enabled = false;
        setupSettings();
    }
    public void setupSettings() {}
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    public void onUpdate() {}
    public void onRender2D(float partialTicks) {}
    public void toggle() { setEnabled(!enabled); }
    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (enabled) onEnable();
            else onDisable();
        }
    }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }
    public boolean isEnabled() { return enabled; }
    public List<Setting> getSettings() { return settings; }
    protected void addSetting(Setting setting) { settings.add(setting); }
}
EOF

# Write Setting.java
cat > src/main/java/myau/client/settings/Setting.java << 'EOF'
package myau.client.settings;
import java.util.Arrays;
import java.util.List;

public class Setting {
    private String name;
    private boolean booleanValue;
    private double doubleValue, min, max, inc;
    private int modeIndex;
    private List<String> modes;
    private SettingType type;
    public enum SettingType { BOOLEAN, SLIDER, MODE }
    public Setting(String name, boolean defaultValue) {
        this.name = name;
        this.type = SettingType.BOOLEAN;
        this.booleanValue = defaultValue;
    }
    public Setting(String name, double defaultValue, double min, double max, double inc) {
        this.name = name;
        this.type = SettingType.SLIDER;
        this.doubleValue = defaultValue;
        this.min = min;
        this.max = max;
        this.inc = inc;
    }
    public Setting(String name, String... modes) {
        this.name = name;
        this.type = SettingType.MODE;
        this.modes = Arrays.asList(modes);
        this.modeIndex = 0;
    }
    public String getName() { return name; }
    public SettingType getType() { return type; }
    public boolean isBoolean() { return type == SettingType.BOOLEAN; }
    public boolean isSlider() { return type == SettingType.SLIDER; }
    public boolean isMode() { return type == SettingType.MODE; }
    public boolean getBooleanValue() { return booleanValue; }
    public void setBooleanValue(boolean val) { booleanValue = val; }
    public void toggle() { booleanValue = !booleanValue; }
    public double getValue() { return doubleValue; }
    public void setValue(double val) {
        doubleValue = Math.max(min, Math.min(max, val));
        doubleValue = Math.round(doubleValue / inc) * inc;
    }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getInc() { return inc; }
    public String getMode() { return modes.get(modeIndex); }
    public int getModeIndex() { return modeIndex; }
    public void setModeIndex(int idx) {
        modeIndex = Math.max(0, Math.min(idx, modes.size() - 1));
    }
    public void cycle() { modeIndex = (modeIndex + 1) % modes.size(); }
    public List<String> getModes() { return modes; }
}
EOF

# Write ModuleManager.java
cat > src/main/java/myau/client/core/ModuleManager.java << 'EOF'
package myau.client.core;

import myau.client.module.Module;
import myau.client.module.impl.Flight;
import myau.client.module.impl.Speed;
import myau.client.module.impl.Sprint;
import myau.client.module.impl.FullBright;
import myau.client.module.impl.KillAura;
import myau.client.module.impl.Scaffold;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private static List<Module> modules = new ArrayList<>();
    public static void init() {
        modules.add(new Flight());
        modules.add(new Speed());
        modules.add(new Sprint());
        modules.add(new FullBright());
        modules.add(new KillAura());
        modules.add(new Scaffold());
    }
    public static List<Module> getModules() { return modules; }
    public static Module getModule(String name) {
        for (Module m : modules) if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }
    public static void onTick() { for (Module m : modules) if (m.isEnabled()) m.onTick(); }
    public static void onUpdate() { for (Module m : modules) if (m.isEnabled()) m.onUpdate(); }
    public static void onRender2D(float partialTicks) { for (Module m : modules) if (m.isEnabled()) m.onRender2D(partialTicks); }
}
EOF

# Write KeybindManager.java
cat > src/main/java/myau/client/core/KeybindManager.java << 'EOF'
package myau.client.core;

import myau.client.module.Module;
import myau.client.gui.ClickGUI;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class KeybindManager {
    private static long lastToggle = 0;
    public static void init() {}
    public static void update() {
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            long now = System.currentTimeMillis();
            if (now - lastToggle > 200) {
                lastToggle = now;
                if (Minecraft.getMinecraft().currentScreen instanceof ClickGUI)
                    Minecraft.getMinecraft().displayGuiScreen(null);
                else
                    Minecraft.getMinecraft().displayGuiScreen(new ClickGUI());
            }
        }
        for (Module m : ModuleManager.getModules()) {
            if (m.getKeybind() > 0 && Keyboard.isKeyDown(m.getKeybind())) {
                if (!m.wasPressed) { m.toggle(); m.wasPressed = true; }
            } else {
                m.wasPressed = false;
            }
        }
    }
    public static void setKeybind(Module m, int key) { if (m != null) m.setKeybind(key); }
}
EOF

# Write ClickGUI.java (short version)
cat > src/main/java/myau/client/gui/ClickGUI.java << 'EOF'
package myau.client.gui;

import myau.client.module.Module;
import myau.client.settings.Setting;
import myau.client.font.CustomFont;
import myau.client.core.ModuleManager;
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
    private int guiX = 100, guiY = 50;
    private int guiWidth = 300, guiHeight = 400;
    private int scrollOffset = 0;
    private Module selectedModule = null;
    private boolean showSettings = false;
    private int settingsWidth = 200, settingsHeight = 250;

    public ClickGUI() {
        instance = this;
        modules = ModuleManager.getModules();
        modules.sort(Comparator.comparing(Module::getName));
        guiX = (Minecraft.getMinecraft().displayWidth / 2) - (guiWidth / 2);
        guiY = (Minecraft.getMinecraft().displayHeight / 2) - (guiHeight / 2);
    }
    @Override
    public void initGui() {
        super.initGui();
        if (CustomFont.TITLE == null) CustomFont.init();
        if (modules.isEmpty()) {
            modules = ModuleManager.getModules();
            modules.sort(Comparator.comparing(Module::getName));
        }
        showSettings = false;
        selectedModule = null;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(guiX, guiY, 0);
        drawRect(0, 0, guiWidth, guiHeight, new Color(20,20,20,220).getRGB());
        drawRect(0, 0, guiWidth, 20, new Color(30,30,30,255).getRGB());
        if (CustomFont.TITLE != null) CustomFont.TITLE.drawString("VANTA", 8, 4, 0xFFFFFF);
        else fontRendererObj.drawString("VANTA", 8, 4, 0xFFFFFF);
        int closeX = guiWidth - 20;
        drawRect(closeX, 2, closeX+16, 18, new Color(200,40,40,200).getRGB());
        fontRendererObj.drawString("X", closeX+4, 4, 0xFFFFFF);
        if (showSettings && selectedModule != null) drawSettingsPanel(mouseX, mouseY, partialTicks);
        else drawModuleList(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    private void drawModuleList(int mouseX, int mouseY, float partialTicks) {
        int yOffset = 24 - scrollOffset;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int scale = Minecraft.getMinecraft().gameSettings.guiScale;
        int scissorX = guiX * scale;
        int scissorY = (Minecraft.getMinecraft().displayHeight - (guiY + guiHeight)) * scale;
        int scissorW = guiWidth * scale;
        int scissorH = (guiHeight - 24) * scale;
        GL11.glScissor(scissorX, scissorY, scissorW, scissorH);
        for (Module m : modules) {
            if (yOffset < 0 || yOffset > guiHeight - 30) { yOffset += 26; continue; }
            int x = 4, w = guiWidth - 8, h = 22;
            Color bg = m.isEnabled() ? new Color(60,120,200,180) : new Color(40,40,40,180);
            drawRect(x, yOffset, x+w, yOffset+h, bg.getRGB());
            if (CustomFont.TITLE != null) CustomFont.TITLE.drawString(m.getName(), x+6, yOffset+4, 0xFFFFFF);
            else fontRendererObj.drawString(m.getName(), x+6, yOffset+4, 0xFFFFFF);
            if (mouseX > guiX+x && mouseX < guiX+x+w && mouseY > guiY+yOffset && mouseY < guiY+yOffset+h)
                drawRect(x, yOffset, x+w, yOffset+h, new Color(255,255,255,40).getRGB());
            yOffset += 26;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
    private void drawSettingsPanel(int mouseX, int mouseY, float partialTicks) {
        int panelX = guiWidth + 4, panelY = 24;
        drawRect(panelX, panelY, panelX+settingsWidth, panelY+settingsHeight, new Color(25,25,25,230).getRGB());
        drawRect(panelX, panelY, panelX+settingsWidth, panelY+20, new Color(40,40,40,255).getRGB());
        if (CustomFont.TITLE != null) CustomFont.TITLE.drawString(selectedModule.getName(), panelX+6, panelY+3, 0xFFFFFF);
        else fontRendererObj.drawString(selectedModule.getName(), panelX+6, panelY+3, 0xFFFFFF);
        int y = panelY + 26;
        for (Setting s : selectedModule.getSettings()) {
            if (s.isBoolean()) { drawBooleanSetting(s, panelX, y, mouseX, mouseY); y += 24; }
            else if (s.isSlider()) { drawSliderSetting(s, panelX, y, mouseX, mouseY); y += 28; }
            else if (s.isMode()) { drawModeSetting(s, panelX, y, mouseX, mouseY); y += 24; }
        }
    }
    private void drawBooleanSetting(Setting s, int x, int y, int mouseX, int mouseY) {
        drawRect(x+4, y, x+150, y+18, new Color(30,30,30,200).getRGB());
        fontRendererObj.drawString(s.getName(), x+8, y+4, 0xCCCCCC);
        boolean value = s.getBooleanValue();
        drawRect(x+130, y+3, x+146, y+15, value ? new Color(60,200,60).getRGB() : new Color(200,60,60).getRGB());
    }
    private void drawSliderSetting(Setting s, int x, int y, int mouseX, int mouseY) {
        drawRect(x+4, y, x+150, y+22, new Color(30,30,30,200).getRGB());
        fontRendererObj.drawString(s.getName() + ": " + String.format("%.1f", s.getValue()), x+8, y+4, 0xCCCCCC);
        int sliderX = x+8, sliderY = y+16, sliderW = 130, sliderH = 4;
        drawRect(sliderX, sliderY, sliderX+sliderW, sliderY+sliderH, new Color(80,80,80).getRGB());
        float percent = (float)((s.getValue() - s.getMin()) / (s.getMax() - s.getMin()));
        drawRect(sliderX, sliderY, sliderX + (int)(sliderW * percent), sliderY+sliderH, new Color(80,180,255).getRGB());
    }
    private void drawModeSetting(Setting s, int x, int y, int mouseX, int mouseY) {
        drawRect(x+4, y, x+150, y+18, new Color(30,30,30,200).getRGB());
        fontRendererObj.drawString(s.getName() + ": " + s.getMode(), x+8, y+4, 0xCCCCCC);
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int relX = mouseX - guiX, relY = mouseY - guiY;
        if (relX > guiWidth-20 && relX < guiWidth-4 && relY > 2 && relY < 18) { close(); return; }
        if (showSettings && selectedModule != null) {
            int panelX = guiWidth + 4, y = 26;
            for (Setting s : selectedModule.getSettings()) {
                if (s.isBoolean()) {
                    if (mouseX > guiX+panelX+4 && mouseX < guiX+panelX+150 && mouseY > guiY+y && mouseY < guiY+y+18) {
                        s.toggle(); return;
                    }
                    y += 24;
                } else if (s.isSlider()) {
                    if (mouseX > guiX+panelX+4 && mouseX < guiX+panelX+150 && mouseY > guiY+y && mouseY < guiY+y+22) {
                        float percent = (float)(mouseX - (guiX+panelX+8)) / 130f;
                        float val = s.getMin() + percent * (s.getMax() - s.getMin());
                        s.setValue(Math.round(val / s.getInc()) * s.getInc());
                        return;
                    }
                    y += 28;
                } else if (s.isMode()) {
                    if (mouseX > guiX+panelX+4 && mouseX < guiX+panelX+150 && mouseY > guiY+y && mouseY < guiY+y+18) {
                        s.cycle(); return;
                    }
                    y += 24;
                }
            }
            return;
        }
        int yOffset = 24 - scrollOffset;
        for (Module m : modules) {
            if (yOffset < 0 || yOffset > guiHeight - 30) { yOffset += 26; continue; }
            int x = 4, w = guiWidth - 8, h = 22;
            if (relX > x && relX < x+w && relY > yOffset && relY < yOffset+h) {
                if (mouseButton == 0) m.toggle();
                else if (mouseButton == 1) { selectedModule = m; showSettings = true; }
                return;
            }
            yOffset += 26;
        }
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
        if (keyCode == 1) { close(); return; }
        if (keyCode == 0x9) { showSettings = false; selectedModule = null; return; }
        super.keyTyped(typedChar, keyCode);
    }
    private void close() {
        Minecraft.getMinecraft().displayGuiScreen(null);
        if (Minecraft.getMinecraft().currentScreen == null)
            Minecraft.getMinecraft().setIngameFocus();
    }
    @Override public boolean doesGuiPauseGame() { return false; }
    public static ClickGUI getInstance() { return instance; }
}
EOF

# Now write the module implementations (Flight, Speed, Sprint, FullBright, KillAura, Scaffold)
cat > src/main/java/myau/client/module/impl/Flight.java << 'EOF'
package myau.client.module.impl;

import myau.client.module.Module;
import myau.client.settings.Setting;

public class Flight extends Module {
    public Flight() { super("Flight", "Movement"); }
    @Override
    public void setupSettings() {
        addSetting(new Setting("Speed", 1.0, 0.1, 5.0, 0.1));
        addSetting(new Setting("Mode", "Vanilla", "Creative", "Glide"));
        addSetting(new Setting("Vertical", true));
    }
    @Override public void onEnable() { mc.thePlayer.capabilities.allowFlying = true; }
    @Override public void onDisable() { mc.thePlayer.capabilities.allowFlying = false; }
}
EOF

cat > src/main/java/myau/client/module/impl/Speed.java << 'EOF'
package myau.client.module.impl;

import myau.client.module.Module;
import myau.client.settings.Setting;

public class Speed extends Module {
    public Speed() { super("Speed", "Movement"); }
    @Override
    public void setupSettings() {
        addSetting(new Setting("Mode", "BHop", "Strafe", "NCP"));
        addSetting(new Setting("Speed", 1.2, 0.5, 3.0, 0.1));
    }
    @Override public void onUpdate() { if (mc.thePlayer.onGround) { /* speed logic */ } }
}
EOF

cat > src/main/java/myau/client/module/impl/Sprint.java << 'EOF'
package myau.client.module.impl;

import myau.client.module.Module;

public class Sprint extends Module {
    public Sprint() { super("Sprint", "Movement"); }
    @Override public void onUpdate() { if (mc.thePlayer.moveForward > 0) mc.thePlayer.setSprinting(true); }
}
EOF

cat > src/main/java/myau/client/module/impl/FullBright.java << 'EOF'
package myau.client.module.impl;

import myau.client.module.Module;

public class FullBright extends Module {
    private float oldGamma;
    public FullBright() { super("FullBright", "Visual"); }
    @Override public void onEnable() { oldGamma = mc.gameSettings.gammaSetting; mc.gameSettings.gammaSetting = 100f; }
    @Override public void onDisable() { mc.gameSettings.gammaSetting = oldGamma; }
}
EOF

cat > src/main/java/myau/client/module/impl/KillAura.java << 'EOF'
package myau.client.module.impl;

import myau.client.module.Module;
import myau.client.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class KillAura extends Module {
    public KillAura() { super("KillAura", "Combat"); }
    @Override public void setupSettings() {
        addSetting(new Setting("Range", 4.0, 1.0, 8.0, 0.1));
        addSetting(new Setting("HitDelay", true));
    }
    @Override public void onUpdate() {
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityPlayer && e != mc.thePlayer) {
                if (mc.thePlayer.getDistanceToEntity(e) < 4.0)
                    mc.playerController.attackEntity(mc.thePlayer, e);
            }
        }
    }
}
EOF

cat > src/main/java/myau/client/module/impl/Scaffold.java << 'EOF'
package myau.client.module.impl;

import myau.client.module.Module;
import myau.client.settings.Setting;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class Scaffold extends Module {
    private Setting mode, range, swing;
    public Scaffold() { super("Scaffold", "Movement"); }
    @Override public void setupSettings() {
        mode = new Setting("Mode", "Normal", "Fast", "Legit");
        range = new Setting("Range", 4.0, 1.0, 6.0, 0.1);
        swing = new Setting("Swing", true);
        addSetting(mode); addSetting(range); addSetting(swing);
    }
    @Override public void onUpdate() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        if (mc.theWorld.isAirBlock(pos)) {
            int slot = getBlockSlot();
            if (slot == -1) return;
            EnumFacing side = getPlaceSide(pos);
            if (side == null) return;
            int oldSlot = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = slot;
            mc.playerController.clickBlock(pos, side);
            if (swing.getBooleanValue()) mc.thePlayer.swingItem();
            mc.thePlayer.inventory.currentItem = oldSlot;
        }
    }
    private int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) return i;
        }
        return -1;
    }
    private EnumFacing getPlaceSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);
            if (!mc.theWorld.isAirBlock(neighbor)) return side.getOpposite();
        }
        return null;
    }
}
EOF

# Write CustomFont and CustomFontRenderer (short versions)
cat > src/main/java/myau/client/font/CustomFont.java << 'EOF'
package myau.client.font;

import java.awt.*;

public class CustomFont {
    public static CustomFontRenderer TITLE, HEADER, BODY, SMALL;
    public static void init() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, CustomFont.class.getResourceAsStream("/assets/amongusclient/fonts/Verdana.ttf"));
            font = font.deriveFont(Font.PLAIN, 22);
            TITLE = new CustomFontRenderer(font);
            HEADER = new CustomFontRenderer(font.deriveFont(17f));
            BODY = new CustomFontRenderer(font.deriveFont(14f));
            SMALL = new CustomFontRenderer(font.deriveFont(12f));
        } catch (Exception e) {
            Font fallback = new Font("Arial", Font.PLAIN, 14);
            TITLE = new CustomFontRenderer(fallback.deriveFont(22f));
            HEADER = new CustomFontRenderer(fallback.deriveFont(17f));
            BODY = new CustomFontRenderer(fallback);
            SMALL = new CustomFontRenderer(fallback.deriveFont(12f));
        }
    }
}
EOF

cat > src/main/java/myau/client/font/CustomFontRenderer.java << 'EOF'
package myau.client.font;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class CustomFontRenderer {
    private Font font;
    private Map<Character, GlyphData> glyphCache = new HashMap<>();
    private int height, ascent;
    public CustomFontRenderer(Font font) {
        this.font = font;
        BufferedImage img = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        this.height = fm.getHeight();
        this.ascent = fm.getAscent();
        g.dispose();
    }
    private GlyphData getGlyphData(char c) {
        if (glyphCache.containsKey(c)) return glyphCache.get(c);
        BufferedImage img = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int charWidth = fm.charWidth(c);
        int charHeight = fm.getHeight();
        g.dispose();
        if (charWidth <= 0) return null;
        int padding = 2;
        int texWidth = charWidth + padding*2;
        int texHeight = charHeight + padding*2;
        img = new BufferedImage(texWidth, texHeight, BufferedImage.TYPE_INT_ARGB);
        g = img.createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setColor(new Color(0,0,0,0));
        g.fillRect(0,0,texWidth,texHeight);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(c), padding, padding + fm.getAscent());
        g.dispose();
        int glId = createGLTexture(img);
        if (glId <= 0) return null;
        GlyphData glyph = new GlyphData();
        glyph.glTextureId = glId;
        glyph.width = texWidth;
        glyph.height = texHeight;
        glyphCache.put(c, glyph);
        return glyph;
    }
    private int createGLTexture(BufferedImage image) {
        int w = image.getWidth(), h = image.getHeight();
        int[] pixels = new int[w*h];
        image.getRGB(0,0,w,h,pixels,0,w);
        ByteBuffer buffer = BufferUtils.createByteBuffer(w*h*4);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = pixels[y*w + x];
                buffer.put((byte)((pixel>>16)&0xFF));
                buffer.put((byte)((pixel>>8)&0xFF));
                buffer.put((byte)(pixel&0xFF));
                buffer.put((byte)((pixel>>24)&0xFF));
            }
        }
        buffer.flip();
        int texId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        return texId;
    }
    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (text == null || text.isEmpty()) return;
        if (shadow) drawString(text, x+1, y+1, darken(color), false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        float curX = x;
        for (int i=0; i<text.length(); i++) {
            GlyphData glyph = getGlyphData(text.charAt(i));
            if (glyph == null) { curX += 6; continue; }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyph.glTextureId);
            GL11.glColor4f((color>>16&0xFF)/255f, (color>>8&0xFF)/255f, (color&0xFF)/255f, (color>>24&0xFF)/255f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0,0); GL11.glVertex2f(curX, y);
            GL11.glTexCoord2f(1,0); GL11.glVertex2f(curX+glyph.width, y);
            GL11.glTexCoord2f(1,1); GL11.glVertex2f(curX+glyph.width, y+glyph.height);
            GL11.glTexCoord2f(0,1); GL11.glVertex2f(curX, y+glyph.height);
            GL11.glEnd();
            curX += glyph.width - 2;
        }
        GL11.glDisable(GL11.GL_BLEND);
    }
    public void drawString(String text, float x, float y, int color) { drawString(text, x, y, color, false); }
    private int darken(int color) {
        int a = color>>24&0xFF;
        int r = Math.max(0, (color>>16&0xFF)-40);
        int g = Math.max(0, (color>>8&0xFF)-40);
        int b = Math.max(0, (color&0xFF)-40);
        return (a<<24)|(r<<16)|(g<<8)|b;
    }
    public int getStringWidth(String text) {
        int width = 0;
        for (int i=0; i<text.length(); i++) {
            GlyphData glyph = getGlyphData(text.charAt(i));
            width += (glyph != null) ? glyph.width - 2 : 6;
        }
        return width;
    }
    public int getHeight() { return height; }
    public int getAscent() { return ascent; }
    public void cleanUp() {
        for (GlyphData glyph : glyphCache.values())
            if (glyph.glTextureId > 0) GL11.glDeleteTextures(glyph.glTextureId);
        glyphCache.clear();
    }
    public static class GlyphData { public int glTextureId, width, height; }
}
EOF

echo "All files created. Now commit and push."
