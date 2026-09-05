package com.amongus.client;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigManager {
    private static final File CONFIG = new File("config.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static JsonObject load() {
        if (!CONFIG.exists()) return defaultConfig();
        try (Reader r = new InputStreamReader(new FileInputStream(CONFIG), StandardCharsets.UTF_8)) {
            return new JsonParser().parse(r).getAsJsonObject();
        } catch (Exception e) { return defaultConfig(); }
    }

    private static JsonObject defaultConfig() {
        JsonObject o = new JsonObject();
        o.addProperty("guiStyle", "Prestige");
        o.addProperty("accentColor", "#E60000");
        return o;
    }

    public static void save(JsonObject obj) {
        try (Writer w = new OutputStreamWriter(new FileOutputStream(CONFIG), StandardCharsets.UTF_8)) {
            gson.toJson(obj, w);
        } catch (Exception ignored) {}
    }
}
