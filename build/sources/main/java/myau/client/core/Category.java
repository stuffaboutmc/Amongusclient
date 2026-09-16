package myau.client.core;

public enum Category {
    COMBAT("Combat", 0xFFE60000),
    PLAYER("Player", 0xFF00FF88),
    MOVEMENT("Movement", 0xFF00AAFF),
    RENDER("Render", 0xFFFFAA00),
    WORLD("World", 0xFF5555FF),
    MISC("Misc", 0xFFAAAAAA),
    EXPLOIT("Exploit", 0xFFFF00FF),
    FUN("Fun", 0xFFFF5555),
    TARGETS("Targets", 0xFF55FFFF);

    public final String displayName;
    public final int color;

    Category(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }
}
