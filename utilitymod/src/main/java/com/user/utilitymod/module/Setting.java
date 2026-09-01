package com.user.utilitymod.module;

/**
 * A single configurable value belonging to a Module.
 * Supports two kinds: SLIDER (numeric range) and TOGGLE (boolean sub-option).
 */
public class Setting {

    public enum Type { SLIDER, TOGGLE }

    private final String name;
    private final Type type;

    // Slider fields
    private double value;
    private final double min;
    private final double max;
    private final double increment;

    // Toggle fields
    private boolean enabled;

    private Setting(String name, Type type, double value, double min, double max, double increment, boolean enabled) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.enabled = enabled;
    }

    public static Setting slider(String name, double value, double min, double max, double increment) {
        return new Setting(name, Type.SLIDER, value, min, max, increment, false);
    }

    public static Setting toggle(String name, boolean defaultValue) {
        return new Setting(name, Type.TOGGLE, 0, 0, 0, 0, defaultValue);
    }

    public String getName() { return name; }
    public Type getType() { return type; }

    public double getValue() { return value; }
    public void setValue(double value) {
        this.value = Math.max(min, Math.min(max, value));
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getIncrement() { return increment; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void toggle() { this.enabled = !this.enabled; }
}
