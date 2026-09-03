package com.amongus.client.modules;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import java.util.ArrayList;
import java.util.List;

public class Module {
    protected Minecraft mc = Minecraft.getMinecraft();
    private String name;
    private int key;
    private boolean enabled;
    private Category category;
    private String description;
    private List<Setting> settings = new ArrayList<>();

    public Module(String name, int key, Category category) {
        this.name = name; this.key = key; this.category = category; this.description = "No description.";
    }

    public Module(String name, int key, Category category, String description) {
        this.name = name; this.key = key; this.category = category; this.description = description;
    }

    public void toggle() {
        enabled = enabled;
        if (enabled) { onEnable(); MinecraftForge.EVENT_BUS.register(this); }
        else { onDisable(); MinecraftForge.EVENT_BUS.unregister(this); }
    }
    public void onEnable() {}
    public void onDisable() {}
    public String getName() { return name; }
    public int getKey() { return key; }
    public boolean isEnabled() { return enabled; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public void setKey(int key) { this.key = key; }
    public List<Setting> getSettings() { return settings; }
    public void addSetting(Setting setting) { settings.add(setting); }

    public Setting getSetting(String name) {
        for (Setting s : settings) if (s.getName().equalsIgnoreCase(name)) return s;
        return null;
    }

    public enum Category { COMBAT, MOVEMENT, RENDER, PLAYER, MISC }

    public static class Setting {
        private String name;
        private String value;
        private String[] options;
        private boolean isSlider;
        private double min, max, increment;

        public Setting(String name, String[] options, String defaultValue) {
            this.name = name; this.options = options; this.value = defaultValue; this.isSlider = false;
        }

        public Setting(String name, double min, double max, double defaultValue, double increment) {
            this.name = name; this.min = min; this.max = max; this.increment = increment;
            this.value = String.valueOf(defaultValue); this.isSlider = true;
        }

        public String getName() { return name; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String[] getOptions() { return options; }
        public boolean isSlider() { return isSlider; }
        public double getMin() { return min; }
        public double getMax() { return max; }
        public double getIncrement() { return increment; }
        public double getDoubleValue() { return Double.parseDouble(value); }
        public void cycle() {
            if (options == null) return;
            int current = -1;
            for (int i = 0; i < options.length; i++) if (options[i].equals(value)) current = i;
            current++;
            if (current >= options.length) current = 0;
            value = options[current];
        }
    }
}
