package myau.client.core;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;

public class Module {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private int key;
    public boolean wasPressed = false;
    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, Category category, int key) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.key = key;
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void onTick() {}
    public void onRender2D(float partialTicks) {}
    public void onRender3D(float partialTicks) {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
    public int getKeybind() { return key; }
    public void setKeybind(int key) { this.key = key; }
    public List<Setting> getSettings() { return settings; }

    public Setting getSetting(String name) {
        for (Setting s : settings) if (s.getName().equalsIgnoreCase(name)) return s;
        return null;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable(); else onDisable();
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) toggle();
    }

    protected void addSetting(Setting s) { settings.add(s); }

    public static class Setting {
        private final String name;
        private final SettingType type;
        private Object value;
        private final Object min, max;
        private final double inc;
        private int modeIndex;
        private java.util.List<String> modes;

        public Setting(String name, SettingType type, Object value) {
            this.name = name; this.type = type; this.value = value; this.min = null; this.max = null;
            this.inc = 0;
        }

        public Setting(String name, SettingType type, Object value, Object min, Object max) {
            this.name = name; this.type = type; this.value = value; this.min = min; this.max = max;
            this.inc = (type == SettingType.NUMBER) ? 1.0 : 0;
        }

        public Setting(String name, SettingType type, Object value, Object min, Object max, String... modes) {
            this.name = name; this.type = type; this.value = value; this.min = min; this.max = max;
            this.modes = java.util.Arrays.asList(modes);
            this.modeIndex = 0;
            this.inc = 0;
        }

        public String getName() { return name; }
        public SettingType getType() { return type; }
        public boolean isBoolean() { return type == SettingType.BOOLEAN; }
        public boolean isSlider() { return type == SettingType.NUMBER; }
        public boolean isMode() { return type == SettingType.MODE; }
        public Object getValue() { return value; }
        public void setValue(Object v) { this.value = v; }
        public Object getMin() { return min; }
        public Object getMax() { return max; }
        public Object getInc() { return inc; }
        public boolean getBoolean() { return (Boolean) value; }
        public boolean getBooleanValue() { return (Boolean) value; }
        public double getDouble() { return ((Number) value).doubleValue(); }
        public float getFloat() { return ((Number) value).floatValue(); }
        public int getInt() { return ((Number) value).intValue(); }

        public String getMode() { return modes != null && !modes.isEmpty() ? modes.get(modeIndex) : String.valueOf(value); }
        public int getModeIndex() { return modeIndex; }
        public void setModeIndex(int idx) {
            if (modes != null) modeIndex = Math.max(0, Math.min(idx, modes.size() - 1));
        }
        public java.util.List<String> getModes() { return modes; }
        public void cycle() {
            if (modes != null && !modes.isEmpty()) modeIndex = (modeIndex + 1) % modes.size();
        }
        public void toggle() {
            if (type == SettingType.BOOLEAN) value = !(Boolean) value;
            else if (type == SettingType.MODE) cycle();
        }
    }

    public enum SettingType { BOOLEAN, NUMBER, MODE }
}
