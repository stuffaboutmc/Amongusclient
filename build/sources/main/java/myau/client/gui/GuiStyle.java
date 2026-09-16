package myau.client.gui;

public enum GuiStyle {
    Rise("Rise", 0xFF2A2A2A, 0xFFE60000),
    Vape("Vape", 0xFF1A1A2E, 0xFF00D4FF),
    Augustus("Augustus", 0xFF0D0D0D, 0xFF6C00B4),
    Prestige("Prestige", 0xFF1A1A1A, 0xFFFFD700);

    public final String displayName;
    public final int bgColor;
    public final int accentColor;

    GuiStyle(String displayName, int bgColor, int accentColor) {
        this.displayName = displayName;
        this.bgColor = bgColor;
        this.accentColor = accentColor;
    }
}
