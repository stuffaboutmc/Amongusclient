package com.user.utilitymod.module;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private final Category category;
    private int keybind; // LWJGL key code, 0 = unbound
    private boolean enabled = false;

    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, Category category) {
        this(name, category, 0);
    }

    public Module(String name, Category category, int keybind) {
        this.name = name;
        this.category = category;
        this.keybind = keybind;
    }

    protected void addSetting(Setting setting) {
        settings.add(setting);
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public Setting getSetting(String name) {
        for (Setting s : settings) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
        notifyToggle(enabled);
    }

    private void notifyToggle(boolean enabled) {
        if (category == Category.RENDER && name.equals("Notifications")) return;
        Module notifications = ModuleManager.getModuleByName("Notifications");
        if (notifications != null && notifications.isEnabled()
                && com.user.utilitymod.event.HudRenderer.INSTANCE != null) {
            com.user.utilitymod.event.HudRenderer.INSTANCE.pushNotification(
                    name + (enabled ? " enabled" : " disabled"));
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    /** Called once when the module is turned on. Override as needed. */
    protected void onEnable() {}

    /** Called once when the module is turned off. Override as needed. */
    protected void onDisable() {}

    /** Called every client tick while enabled. Override as needed. */
    public void onTick() {}

    /** Short status string shown next to the module name in the HUD overlay, or null. */
    public String getSuffix() {
        return null;
    }

    protected boolean isPlayerValid() {
        return mc.thePlayer != null && mc.theWorld != null;
    }
}
