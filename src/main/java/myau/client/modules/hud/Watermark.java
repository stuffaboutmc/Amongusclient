package myau.client.modules.hud;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class Watermark extends Module {
    public Watermark() {
        super("Watermark", "Shows client name on screen", Category.HUD, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender2D(float partialTicks) {
        mc.fontRendererObj.drawStringWithShadow("among us client", 4, 4, 0xFFFFFF);
    }
}
