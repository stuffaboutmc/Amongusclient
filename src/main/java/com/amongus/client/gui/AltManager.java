package com.amongus.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AltManager {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static List<Alt> alts = new ArrayList<>();
    private static File altFile;

    public static class Alt {
        public String name;
        public String email;
        public String password;
        public String token;
        public String sessionId;
        public boolean isTokenAlt;

        public Alt(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.isTokenAlt = false;
        }

        public Alt(String name, String token, boolean isToken) {
            this.name = name;
            this.token = token;
            this.sessionId = token;
            this.isTokenAlt = true;
        }
    }

    public static void init() {
        altFile = new File(mc.mcDataDir, "amongus_alts.json");
        loadAlts();
    }

    public static void loadAlts() {
        if (!altFile.exists()) return;
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Alt>>(){}.getType();
            alts = gson.fromJson(new FileReader(altFile), listType);
            if (alts == null) alts = new ArrayList<>();
        } catch (Exception e) {
            alts = new ArrayList<>();
        }
    }

    public static void saveAlts() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(altFile);
            gson.toJson(alts, writer);
            writer.close();
        } catch (Exception e) {}
    }

    public static void addAlt(String name, String email, String password) {
        alts.add(new Alt(name, email, password));
        saveAlts();
    }

    public static void addTokenAlt(String name, String token) {
        alts.add(new Alt(name, token, true));
        saveAlts();
    }

    public static void removeAlt(int index) {
        if (index >= 0 && index < alts.size()) {
            alts.remove(index);
            saveAlts();
        }
    }

    public static void loginToAlt(Alt alt) {
        try {
            Session session;
            if (alt.isTokenAlt) {
                session = new Session(alt.name, alt.token, "legacy", "legacy");
            } else {
                session = new Session(alt.name, "offline", "offline", "offline");
            }
            setSession(session);
        } catch (Exception e) {}
    }

    public static void loginOffline(String name) {
        try {
            setSession(new Session(name, "offline", "offline", "offline"));
        } catch (Exception e) {}
    }

    private static void setSession(Session session) throws Exception {
        Field f = Minecraft.class.getDeclaredField("session");
        f.setAccessible(true);
        f.set(mc, session);
    }

    public static List<Alt> getAlts() { return alts; }
}
