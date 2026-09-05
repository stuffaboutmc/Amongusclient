package myau.client.gui.font;

import java.awt.*;

public class CustomFont {

    public static CustomFontRenderer TITLE;
    public static CustomFontRenderer HEADER;
    public static CustomFontRenderer BODY;
    public static CustomFontRenderer SMALL;

    public static void init() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, CustomFont.class.getResourceAsStream("/assets/amongusclient/fonts/Verdana.ttf"));
            font = font.deriveFont(Font.PLAIN, 22);
            TITLE = new CustomFontRenderer(font);
            HEADER = new CustomFontRenderer(font.deriveFont(17f));
            BODY = new CustomFontRenderer(font.deriveFont(14f));
            SMALL = new CustomFontRenderer(font.deriveFont(12f));
        } catch (Exception e) {
            Font fallback = new Font("Arial", Font.PLAIN, 14);
            TITLE = new CustomFontRenderer(fallback.deriveFont(22f));
            HEADER = new CustomFontRenderer(fallback.deriveFont(17f));
            BODY = new CustomFontRenderer(fallback);
            SMALL = new CustomFontRenderer(fallback.deriveFont(12f));
        }
    }
}
