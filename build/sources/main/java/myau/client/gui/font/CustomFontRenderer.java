package myau.client.gui.font;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class CustomFontRenderer {

    private Font font;
    private Map<Character, GlyphData> glyphCache = new HashMap<>();
    private int height;
    private int ascent;

    public CustomFontRenderer(Font font) {
        this.font = font;
        initMetrics();
    }

    private void initMetrics() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        this.height = fm.getHeight();
        this.ascent = fm.getAscent();
        g.dispose();
    }

    private GlyphData getGlyphData(char c) {
        if (glyphCache.containsKey(c)) return glyphCache.get(c);

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int charWidth = fm.charWidth(c);
        int charHeight = fm.getHeight();
        g.dispose();

        if (charWidth <= 0) return null;

        int padding = 2;
        int texWidth = charWidth + padding * 2;
        int texHeight = charHeight + padding * 2;

        img = new BufferedImage(texWidth, texHeight, BufferedImage.TYPE_INT_ARGB);
        g = img.createGraphics();
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, texWidth, texHeight);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(c), padding, padding + fm.getAscent());
        g.dispose();

        int glId = createGLTexture(img);
        if (glId <= 0) return null;

        GlyphData glyph = new GlyphData();
        glyph.glTextureId = glId;
        glyph.width = texWidth;
        glyph.height = texHeight;
        glyphCache.put(c, glyph);
        return glyph;
    }

    private int createGLTexture(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        int[] pixels = new int[w * h];
        image.getRGB(0, 0, w, h, pixels, 0, w);

        ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = pixels[y * w + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();

        int texId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, w, h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        return texId;
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (text == null || text.isEmpty()) return;
        if (shadow) drawString(text, x + 1, y + 1, darken(color), false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        float curX = x;
        for (int i = 0; i < text.length(); i++) {
            GlyphData glyph = getGlyphData(text.charAt(i));
            if (glyph == null) { curX += 6; continue; }
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyph.glTextureId);
            GL11.glColor4f(
                ((color >> 16) & 0xFF) / 255f,
                ((color >> 8) & 0xFF) / 255f,
                (color & 0xFF) / 255f,
                ((color >> 24) & 0xFF) / 255f
            );
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(curX, y);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(curX + glyph.width, y);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(curX + glyph.width, y + glyph.height);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(curX, y + glyph.height);
            GL11.glEnd();
            curX += glyph.width - 2;
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawString(String text, float x, float y, int color) {
        drawString(text, x, y, color, false);
    }

    private int darken(int color) {
        int a = color >> 24 & 0xFF;
        int r = Math.max(0, (color >> 16 & 0xFF) - 40);
        int g = Math.max(0, (color >> 8 & 0xFF) - 40);
        int b = Math.max(0, (color & 0xFF) - 40);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public int getStringWidth(String text) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            GlyphData glyph = getGlyphData(text.charAt(i));
            width += (glyph != null) ? glyph.width - 2 : 6;
        }
        return width;
    }

    public int getHeight() { return height; }
    public int getAscent() { return ascent; }

    public void cleanUp() {
        for (GlyphData glyph : glyphCache.values()) {
            if (glyph.glTextureId > 0) GL11.glDeleteTextures(glyph.glTextureId);
        }
        glyphCache.clear();
    }

    public static class GlyphData {
        public int glTextureId;
        public int width;
        public int height;
    }
}
