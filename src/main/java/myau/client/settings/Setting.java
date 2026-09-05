package myau.client.settings;

import java.util.Arrays;
import java.util.List;

public class Setting {

    private String name;
    private boolean booleanValue;
    private double doubleValue;
    private double min, max, inc;
    private int modeIndex;
    private List<String> modes;
    private SettingType type;

    public enum SettingType {
        BOOLEAN, SLIDER, MODE
    }

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
    public void cycle() {
        modeIndex = (modeIndex + 1) % modes.size();
    }
    public List<String> getModes() { return modes; }
}
