package myau.client.modules.hud;

import myau.client.core.Category;
import myau.client.core.Module;
import myau.client.core.ModuleManager;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArrayListMod extends Module {
    public ArrayListMod() {
        super("ArrayList", "Shows enabled modules on screen", Category.HUD, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender2D(float partialTicks) {
        List<Module> enabled = new ArrayList<>();
        for (Module m : ModuleManager.getModules()) {
            if (m.isEnabled() && m.getCategory() != Category.HUD) {
                enabled.add(m);
            }
        }

        enabled.sort(new Comparator<Module>() {
            @Override
            public int compare(Module a, Module b) {
                return mc.fontRendererObj.getStringWidth(b.getName()) - mc.fontRendererObj.getStringWidth(a.getName());
            }
        });

        int y = 2;
        int x = mc.displayWidth / 2 - 1;

        for (Module m : enabled) {
            String name = m.getName();
            int color = m.getCategory().color;
            mc.fontRendererObj.drawStringWithShadow(name, x - mc.fontRendererObj.getStringWidth(name), y, color);
            y += mc.fontRendererObj.FONT_HEIGHT + 2;
        }
    }
}
