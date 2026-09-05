package myau.client.gui.font;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class CustomFontRenderer {
    private Font font;
    private int fontSize;
    private Map<Character, GlyphData> glyphCache = new HashMap<>();
    private FontMetrics metrics;
    private boolean initialized = false;

    private static class GlyphData {
        int glTextureId;
        int width;
        int height;
    }

    public CustomFontRenderer(String fontName, int size) {
        this.fontSize = size;
        try {
            Font testFont = new Font(fontName, Font.PLAIN, size);
            if (testFont.canDisplay('A') && testFont.getFamily().equalsIgnoreCase(fontName)) {
                this.font = testFont;
            } else {
                this.font = new Font(Font.SANS_SERIF, Font.PLAIN, size);
            }
        } catch (Exception e) {
            this.font = new Font(Font.SANS_SERIF, Font.PLAIN, size);
        }
        initMetrics();
    }

    private void initMetrics() {
        BufferedImage tmp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = tmp.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setFont(font);
        metrics = g.getFontMetrics();
        g.dispose();
        initialized = true;
    }

    public float drawString(String text, float x, float y, int color, boolean shadow) {
        if (shadow) {
            drawStringInternal(text, x + 1f, y + 1f, darkenColor(color));
        }
        return drawStringInternal(text, x, y, color);
    }

    private float drawStringInternal(String text, float x, float y, int color) {
        if (text == null || text.isEmpty()) return x;
        float currentX = x;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            GlyphData glyph = getGlyph(c);
            if (glyph != null && glyph.glTextureId > 0) {
                float a = (color >> 24 & 0xFF) / 255f;
                float r = (color >> 16 & 0xFF) / 255f;
                float g = (color >> 8 & 0xFF) / 255f;
                float b = (color & 0xFF) / 255f;

                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyph.glTextureId);
                GL11.glColor4f(r, g, b, a);

                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0, 0);
                GL11.glVertex2f(currentX, y);
                GL11.glTexCoord2f(1, 0);
                GL11.glVertex2f(currentX + glyph.width, y);
                GL11.glTexCoord2f(1, 1);
                GL11.glVertex2f(currentX + glyph.width, y + glyph.height);
                GL11.glTexCoord2f(0, 1);
                GL11.glVertex2f(currentX, y + glyph.height);
                GL11.glEnd();

                GL11.glColor4f(1, 1, 1, 1);
                currentX += glyph.width;
            } else {
                currentX += fontSize / 2;
            }
        }
        return currentX;
    }

    private GlyphData getGlyph(char c) {
        if (glyphCache.containsKey(c)) {
            GlyphData cached = glyphCache.get(c);
            if (cached.glTextureId > 0 && GL11.glIsTexture(cached.glTextureId)) {
                return cached;
            }
            glyphCache.remove(c);
        }

        int padding = 2;
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int charWidth = fm.charWidth(c);
        int charHeight = fm.getHeight();
        g.dispose();

        if (charWidth <= 0) {
            charWidth = fm.charWidth(' ');
        }
        if (charHeight <= 0) {
            charHeight = fontSize;
        }

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

    public int getStringWidth(String text) {
        if (text == null || text.isEmpty()) return 0;
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (metrics != null) {
                width += metrics.charWidth(c);
            } else {
                width += fontSize / 2;
            }
        }
        return width;
    }

    public int getHeight() {
        return metrics != null ? metrics.getHeight() : fontSize;
    }

    public int getAscent() {
        return metrics != null ? metrics.getAscent() : fontSize;
    }

    private int darkenColor(int color) {
        int a = color >> 24 & 0xFF;
        int r = Math.max(0, (color >> 16 & 0xFF) - 40);
        int g = Math.max(0, (color >> 8 & 0xFF) - 40);
        int b = Math.max(0, (color & 0xFF) - 40);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public void cleanUp() {
        for (GlyphData glyph : glyphCache.values()) {
            if (glyph.glTextureId > 0) {
                GL11.glDeleteTextures(glyph.glTextureId);
            }
        }
        glyphCache.clear();
    }
}
