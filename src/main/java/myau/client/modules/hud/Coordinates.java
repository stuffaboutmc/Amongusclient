package myau.client.modules.hud;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;

public class Coordinates extends Module {
    public Coordinates() {
        super("Coordinates", "Shows XYZ coordinates", Category.HUD, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender2D(float partialTicks) {
        if (mc.thePlayer == null) return;

        int x = (int) mc.thePlayer.posX;
        int y = (int) mc.thePlayer.posY;
        int z = (int) mc.thePlayer.posZ;
        int bx = (int) Math.floor(mc.thePlayer.posX);
        int bz = (int) Math.floor(mc.thePlayer.posZ);

        String text = String.format("XYZ: %d, %d, %d", x, y, z);
        String blockText = String.format("Block: %d, %d", bx, bz);

        mc.fontRendererObj.drawStringWithShadow(text, 4, 16, 0xAAAAAA);
        mc.fontRendererObj.drawStringWithShadow(blockText, 4, 26, 0x888888);
    }
}
