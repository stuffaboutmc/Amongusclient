package com.user.utilitymod.module;

public enum Category {
    MOVEMENT("Movement"),
    RENDER("Render"),
    COMBAT("Combat"),
    MISC("Misc");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
