package myau.client.module;

import myau.client.settings.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private String name;
    private String category;
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

    public void toggle() {
        setEnabled(!enabled);
    }

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

    protected void addSetting(Setting setting) {
        settings.add(setting);
    }
}
