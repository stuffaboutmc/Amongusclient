package myau.client.gui.font;

import java.awt.Font;

public class CustomFont {
    public static CustomFontRenderer TITLE;
    public static CustomFontRenderer HEADER;
    public static CustomFontRenderer BODY;
    public static CustomFontRenderer SMALL;

    public static void init() {
        String fontName = findFont();
        TITLE = new CustomFontRenderer(fontName, 22);
        HEADER = new CustomFontRenderer(fontName, 17);
        BODY = new CustomFontRenderer(fontName, 14);
        SMALL = new CustomFontRenderer(fontName, 12);
    }

    private static String findFont() {
        String[] candidates = {"Helvetica Neue", "SF Pro Display", "Segoe UI", "Arial", "SansSerif"};
        for (String name : candidates) {
            Font f = new Font(name, Font.PLAIN, 12);
            if (f.canDisplay('A') && f.getFamily() != null && !f.getFamily().equals("Dialog")) {
                return name;
            }
        }
        return "SansSerif";
    }
}
