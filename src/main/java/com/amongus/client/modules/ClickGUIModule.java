package com.amongus.client.modules;

import com.amongus.client.gui.RiseClickGUI;
import net.minecraft.client.Minecraft;

public class ClickGUIModule extends Module {

    // Style enum – same as in the GUI
    public enum Style { RISE, VAPE, AUGUSTUS, PRESTIGE }

    // Property – this will appear in the expanded settings
    @RiseClickGUI.Property
    public Style guiStyle = Style.RISE;

    public ClickGUIModule() {
        super("ClickGUI", Category.RENDER);
        this.setKeyBind(54); // default: Right Shift (keycode 54)
    }

    @Override
    public void onEnable() {
        // Open the GUI when toggled on
        Minecraft.getMinecraft().displayGuiScreen(new RiseClickGUI());
        // Immediately disable again so it acts like a trigger
        this.toggle(); // this will call onDisable automatically
    }

    @Override
    public void onDisable() {
        // Nothing needed – GUI closes when player presses ESC
    }

    // Static method for the GUI to read the current style
    public static Style getCurrentStyle() {
        // Find the ClickGUIModule instance
        for (Module mod : ModuleManager.modules) {
            if (mod instanceof ClickGUIModule) {
                return ((ClickGUIModule) mod).guiStyle;
            }
        }
        return Style.RISE; // fallback
    }
}
