package myau.client.modules.hud;

import myau.client.core.Category;
import myau.client.core.Module;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Keystrokes extends Module {
    public Keystrokes() {
        super("Keystrokes", "Shows WASD + click display", Category.HUD, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender2D(float partialTicks) {
        if (mc.thePlayer == null) return;

        int x = 4;
        int y = 30;
        int size = 20;
        int gap = 2;

        drawKey("W", x + size + gap, y, Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
        drawKey("A", x, y + size + gap, Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
        drawKey("S", x + size + gap, y + size + gap, Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
        drawKey("D", x + (size + gap) * 2, y + size + gap, Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));

        boolean lmb = Mouse.isButtonDown(0);
        boolean rmb = Mouse.isButtonDown(1);
        drawKey("LMB", x, y + (size + gap) * 2 + 4, lmb);
        drawKey("RMB", x + size * 2 + gap * 2, y + (size + gap) * 2 + 4, rmb);
    }

    private void drawKey(String key, int x, int y, boolean pressed) {
        int bgColor = pressed ? 0x80FFFFFF : 0x40000000;
        int textColor = pressed ? 0xFF000000 : 0xFFFFFFFF;

        Gui.drawRect(x, y, x + 18, y + 18, bgColor);
        mc.fontRendererObj.drawStringWithShadow(key, x + 9 - mc.fontRendererObj.getStringWidth(key) / 2, y + 5, textColor);
    }
}
