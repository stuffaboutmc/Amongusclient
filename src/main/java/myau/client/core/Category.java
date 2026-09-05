package myau.client.core;

public enum Category {
    COMBAT("Combat", 0xFFE60000),
    MOVEMENT("Movement", 0xFF00AAFF),
    PLAYER("Player", 0xFF00FF88),
    RENDER("Render", 0xFFFFAA00),
    EXPLOIT("Exploit", 0xFFFF00FF),
    HUD("HUD", 0xFFAAAAAA);

    public final String displayName;
    public final int color;

    Category(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }
}
