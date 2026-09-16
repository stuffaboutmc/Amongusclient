package myau.client.gui;

import myau.client.gui.font.CustomFont;
import myau.client.gui.font.CustomFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ClickGuiRenderer {

    public static void drawRoundedRect(float x, float y, float w, float h, float radius, int color) {
        if (w <= 0 || h <= 0) return;
        if (radius <= 0) {
            drawRect((int) x, (int) y, (int) (x + w), (int) (y + h), color);
            return;
        }
        radius = Math.min(radius, Math.min(w / 2f, h / 2f));
        float a = (color >> 24 & 0xFF) / 255f;
        float r = (color >> 16 & 0xFF) / 255f;
        float g = (color >> 8 & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, a);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x + radius, y);
        GL11.glVertex2f(x + w - radius, y);
        GL11.glVertex2f(x + w - radius, y + h);
        GL11.glVertex2f(x + radius, y + h);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y + radius);
        GL11.glVertex2f(x + radius, y + radius);
        GL11.glVertex2f(x + radius, y + h - radius);
        GL11.glVertex2f(x, y + h - radius);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x + w - radius, y + radius);
        GL11.glVertex2f(x + w, y + radius);
        GL11.glVertex2f(x + w, y + h - radius);
        GL11.glVertex2f(x + w - radius, y + h - radius);
        GL11.glEnd();

        drawCircleSector(x + radius, y + radius, radius, 180, 270);
        drawCircleSector(x + w - radius, y + radius, radius, 270, 360);
        drawCircleSector(x + w - radius, y + h - radius, radius, 0, 90);
        drawCircleSector(x + radius, y + h - radius, radius, 90, 180);

        GlStateManager.enableTexture2D();
    }

    private static void drawCircleSector(float cx, float cy, float radius, int startDeg, int endDeg) {
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(cx, cy);
        for (int deg = startDeg; deg <= endDeg; deg += 3) {
            double rad = Math.toRadians(deg);
            GL11.glVertex2f(cx + (float) (Math.cos(rad) * radius), cy + (float) (Math.sin(rad) * radius));
        }
        double rad = Math.toRadians(endDeg);
        GL11.glVertex2f(cx + (float) (Math.cos(rad) * radius), cy + (float) (Math.sin(rad) * radius));
        GL11.glEnd();
    }

    public static void drawRoundedRectWithShadow(float x, float y, float w, float h, float radius, int color, int shadowColor, int shadowSize) {
        drawRoundedRect(x - shadowSize, y - shadowSize, w + shadowSize * 2, h + shadowSize * 2, radius + shadowSize, shadowColor);
        drawRoundedRect(x, y, w, h, radius, color);
    }

    public static void drawRect(int x1, int y1, int x2, int y2, int color) {
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glColor4f(r, g, b, a);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(x1, y2, 0);
        GL11.glVertex3d(x2, y2, 0);
        GL11.glVertex3d(x2, y1, 0);
        GL11.glVertex3d(x1, y1, 0);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
    }

    public static void drawString(String text, float x, float y, int color) {
        drawString(text, x, y, color, true);
    }

    public static void drawString(String text, float x, float y, int color, boolean shadow) {
        if (CustomFont.BODY != null) {
            CustomFont.BODY.drawString(text, x, y, color, shadow);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        }
    }

    public static void drawString(CustomFontRenderer font, String text, float x, float y, int color, boolean shadow) {
        if (font != null) {
            font.drawString(text, x, y, color, shadow);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
        }
    }

    public static int getStringWidth(String text) {
        if (CustomFont.BODY != null) {
            return CustomFont.BODY.getStringWidth(text);
        }
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

    public static int getStringWidth(CustomFontRenderer font, String text) {
        if (font != null) return font.getStringWidth(text);
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

    public static int getFontHeight() {
        if (CustomFont.BODY != null) return CustomFont.BODY.getHeight();
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    public static int getFontHeight(CustomFontRenderer font) {
        if (font != null) return font.getHeight();
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    public static int getScaledWidth() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.displayWidth <= 0 || mc.displayHeight <= 0) return 0;
        return new ScaledResolution(mc).getScaledWidth();
    }

    public static int getScaledHeight() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.displayWidth <= 0 || mc.displayHeight <= 0) return 0;
        return new ScaledResolution(mc).getScaledHeight();
    }

    public static void drawGradientRect(int x1, int y1, int x2, int y2, int topColor, int bottomColor) {
        float ta = (topColor >> 24 & 255) / 255.0F;
        float tr = (topColor >> 16 & 255) / 255.0F;
        float tg = (topColor >> 8 & 255) / 255.0F;
        float tb = (topColor & 255) / 255.0F;
        float ba = (bottomColor >> 24 & 255) / 255.0F;
        float br = (bottomColor >> 16 & 255) / 255.0F;
        float bg = (bottomColor >> 8 & 255) / 255.0F;
        float bb = (bottomColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(tr, tg, tb, ta);
        GL11.glVertex3d(x1, y1, 0);
        GL11.glColor4f(br, bg, bb, ba);
        GL11.glVertex3d(x1, y2, 0);
        GL11.glColor4f(br, bg, bb, ba);
        GL11.glVertex3d(x2, y2, 0);
        GL11.glColor4f(tr, tg, tb, ta);
        GL11.glVertex3d(x2, y1, 0);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
    }
}
