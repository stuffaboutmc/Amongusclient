package myau.client.modules.hud;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class FPS extends Module {
    public FPS() {
        super("FPS", "Shows FPS counter", Category.HUD, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender2D(float partialTicks) {
        if (mc.thePlayer == null) return;

        int fps = mc.getDebugFPS();
        String text = "FPS: " + fps;
        int color = fps >= 60 ? 0x55FF55 : (fps >= 30 ? 0xFFFF55 : 0xFF5555);
        mc.fontRendererObj.drawStringWithShadow(text, 4, 36, color);
    }
}
