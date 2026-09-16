package myau.client.config;

import com.google.gson.*;
import java.io.*;
import myau.client.core.Module;
import myau.client.core.ModuleManager;
import myau.client.gui.ClickGUI;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File dir;

    public static void init() {
        dir = new File(System.getProperty("user.home"), ".amongusclient/config");
        dir.mkdirs();
        load("default");
    }

    public static void save(String name) {
        JsonObject root = new JsonObject();
        JsonObject modules = new JsonObject();
        for (Module m : ModuleManager.getModules()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("enabled", m.isEnabled());
            obj.addProperty("key", m.getKey());
            modules.add(m.getName(), obj);
        }
        root.add("modules", modules);
        
        JsonObject gui = new JsonObject();
        gui.addProperty("scale", ClickGUI.getInstance().getGuiScale());
        gui.addProperty("firstTimeDone", ClickGUI.getInstance().isFirstTimeDone());
        root.add("gui", gui);
        
        try (FileWriter fw = new FileWriter(new File(dir, name + ".json"))) {
            GSON.toJson(root, fw);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void load(String name) {
        File f = new File(dir, name + ".json");
        if (!f.exists()) return;
        try {
            JsonObject root = GSON.fromJson(new FileReader(f), JsonObject.class);
            if (root == null) return;
            
            if (root.has("modules")) {
                JsonObject modules = root.getAsJsonObject("modules");
                for (Module m : ModuleManager.getModules()) {
                    if (modules.has(m.getName())) {
                        JsonObject obj = modules.getAsJsonObject(m.getName());
                        if (obj.has("enabled") && obj.get("enabled").getAsBoolean()) m.toggle();
                        if (obj.has("key")) m.setKey(obj.get("key").getAsInt());
                    }
                }
            }
            
            if (root.has("gui")) {
                JsonObject gui = root.getAsJsonObject("gui");
                if (gui.has("scale")) {
                    ClickGUI.getInstance().setGuiScale(gui.get("scale").getAsFloat());
                }
                if (gui.has("firstTimeDone")) {
                    ClickGUI.getInstance().setFirstTimeDone(gui.get("firstTimeDone").getAsBoolean());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
