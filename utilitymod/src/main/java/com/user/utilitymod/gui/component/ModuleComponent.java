package com.user.utilitymod.gui.component;

import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.Setting;

public class ModuleComponent {

    public final Module module;
    public boolean settingsOpen = false;
    public boolean draggingSlider = false;
    public Setting activeDragSetting = null;

    private static final double ROW_HEIGHT = 12;
    private static final double SETTING_ROW_HEIGHT = 11;

    public ModuleComponent(Module module) {
        this.module = module;
    }

    public double getHeight() {
        double h = ROW_HEIGHT;
        if (settingsOpen) {
            h += module.getSettings().size() * SETTING_ROW_HEIGHT;
        }
        return h;
    }

    public double getRowHeight() {
        return ROW_HEIGHT;
    }

    public double getSettingRowHeight() {
        return SETTING_ROW_HEIGHT;
    }
}
