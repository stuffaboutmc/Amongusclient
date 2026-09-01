package com.user.utilitymod.gui.component;

import com.user.utilitymod.module.Category;
import com.user.utilitymod.module.Module;
import com.user.utilitymod.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * One draggable panel representing a single Category, containing a
 * ModuleComponent row for each module in that category.
 */
public class Frame {

    public final Category category;
    public double x;
    public double y;
    public double width = 120;
    public boolean collapsed = false;

    public final List<ModuleComponent> components = new ArrayList<>();

    public boolean dragging = false;
    private double dragOffsetX;
    private double dragOffsetY;

    public Frame(Category category, double x, double y) {
        this.category = category;
        this.x = x;
        this.y = y;

        for (Module m : ModuleManager.getModulesByCategory(category)) {
            components.add(new ModuleComponent(m));
        }
    }

    public double getHeaderHeight() {
        return 14;
    }

    public double getTotalHeight() {
        if (collapsed) return getHeaderHeight();
        double h = getHeaderHeight();
        for (ModuleComponent c : components) {
            h += c.getHeight();
        }
        return h;
    }

    public boolean isMouseOverHeader(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + getHeaderHeight();
    }

    public void startDrag(int mouseX, int mouseY) {
        dragging = true;
        dragOffsetX = mouseX - x;
        dragOffsetY = mouseY - y;
    }

    public void stopDrag() {
        dragging = false;
    }

    public void updateDrag(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragOffsetX;
            y = mouseY - dragOffsetY;
        }
    }
}
