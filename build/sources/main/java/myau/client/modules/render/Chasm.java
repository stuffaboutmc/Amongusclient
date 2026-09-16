package myau.client.modules.render;

import myau.client.core.Category;
import myau.client.core.Module;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Chasm extends Module {
    public Chasm() {
        super("Chasm", "Void shader effect at bottom of screen", Category.RENDER, Keyboard.KEY_NONE);
    }

    @Override
    public void onRender2D(float partialTicks) {
        if (mc.thePlayer == null) return;

        int width = mc.displayWidth;
        int height = mc.displayHeight;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        int gradientHeight = 50;
        int screenHeight = mc.displayHeight;

        for (int i = 0; i < gradientHeight; i++) {
            float alpha = 1.0F - ((float) i / gradientHeight);

            GL11.glColor4f(0.0F, 0.0F, 0.0F, alpha);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3d(0, screenHeight - i, 0);
            GL11.glVertex3d(width, screenHeight - i, 0);
            GL11.glVertex3d(width, screenHeight - i - 1, 0);
            GL11.glVertex3d(0, screenHeight - i - 1, 0);
            GL11.glEnd();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
