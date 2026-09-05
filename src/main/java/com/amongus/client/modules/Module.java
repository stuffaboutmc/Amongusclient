package com.amongus.client.modules;

import net.minecraft.client.Minecraft;

public abstract class Module {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private String name;
    private Category category;
    private boolean enabled;
    private int keyBind = 0;

    public Module(String name, Category cat) {
        this.name = name;
        this.category = cat;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void onRender() {}
    public void onRender3D(float pt) {}

    public boolean isEnabled() { return enabled; }
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public int getKeyBind() { return keyBind; }
    public void setKeyBind(int key) { this.keyBind = key; }
}
