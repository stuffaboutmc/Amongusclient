package com.amongus.client.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static List<Theme> themes = new ArrayList<>();
    private static Theme currentTheme;

    public static class Theme {
        public String name;
        public Color background, header, outline, enabled, disabled, accent, text;

        public Theme(String name, Color bg, Color h, Color o, Color e, Color d, Color a, Color t) {
            this.name = name;
            this.background = bg;
            this.header = h;
            this.outline = o;
            this.enabled = e;
            this.disabled = d;
            this.accent = a;
            this.text = t;
        }
    }

    public static void init() {
        themes.add(new Theme("Augustus", new Color(25,25,25,230), new Color(35,35,35,255), new Color(60,60,60,255), new Color(0,255,128,255), new Color(180,180,180,255), new Color(255,0,0,255), new Color(255,255,255,255)));
        themes.add(new Theme("Midnight", new Color(10,10,25,230), new Color(15,15,35,255), new Color(40,40,70,255), new Color(0,180,255,255), new Color(120,120,140,255), new Color(100,50,255,255), new Color(220,220,255,255)));
        themes.add(new Theme("Blood", new Color(30,10,10,230), new Color(40,15,15,255), new Color(80,30,30,255), new Color(255,50,50,255), new Color(150,100,100,255), new Color(255,0,0,255), new Color(255,200,200,255)));
        themes.add(new Theme("Forest", new Color(10,25,10,230), new Color(15,35,15,255), new Color(30,60,30,255), new Color(50,255,50,255), new Color(100,150,100,255), new Color(0,200,0,255), new Color(200,255,200,255)));
        themes.add(new Theme("Ocean", new Color(10,15,30,230), new Color(15,20,40,255), new Color(30,40,80,255), new Color(50,150,255,255), new Color(100,120,160,255), new Color(0,100,255,255), new Color(180,200,255,255)));
        currentTheme = themes.get(0);
    }

    public static void setTheme(String name) {
        for (Theme t : themes) {
            if (t.name.equalsIgnoreCase(name)) {
                currentTheme = t;
                return;
            }
        }
    }

    public static Theme getCurrentTheme() { return currentTheme; }
    public static List<Theme> getThemes() { return themes; }
}
