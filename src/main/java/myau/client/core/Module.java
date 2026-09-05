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

        public Setting(String name, SettingType type, Object value, Object min, Object max) {
            this.name = name; this.type = type; this.value = value; this.min = min; this.max = max;
        }

        public String getName() { return name; }
        public SettingType getType() { return type; }
        public Object getValue() { return value; }
        public void setValue(Object v) { this.value = v; }
        public Object getMin() { return min; }
        public Object getMax() { return max; }
        public boolean getBoolean() { return (Boolean) value; }
        public double getDouble() { return ((Number) value).doubleValue(); }
        public float getFloat() { return ((Number) value).floatValue(); }
        public int getInt() { return ((Number) value).intValue(); }
    }

    public enum SettingType { BOOLEAN, NUMBER, MODE }
}
