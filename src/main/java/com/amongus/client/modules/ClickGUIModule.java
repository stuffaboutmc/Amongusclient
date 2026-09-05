package com.amongus.client.modules;

import com.amongus.client.gui.RiseClickGUI;
import net.minecraft.client.Minecraft;

public class ClickGUIModule extends Module {

    // Style enum with lowercase display names
    public enum Style {
        RISE("rise"),
        VAPE("vape"),
        AUGUSTUS("augustus"),
        PRESTIGE("prestige");

        private final String displayName;

        Style(String name) {
            this.displayName = name;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    @RiseClickGUI.Property
    public Style guiStyle = Style.RISE;

    public ClickGUIModule() {
        super("ClickGUI", Category.RENDER);
        this.setKeyBind(54); // Right Shift
    }

    @Override
    public void onEnable() {
        Minecraft.getMinecraft().displayGuiScreen(new RiseClickGUI());
        // toggle back off so it acts as a trigger
        this.toggle();
    }

    public static Style getCurrentStyle() {
        for (Module mod : ModuleManager.modules) {
            if (mod instanceof ClickGUIModule) {
                return ((ClickGUIModule) mod).guiStyle;
            }
        }
        return Style.RISE;
    }
}
