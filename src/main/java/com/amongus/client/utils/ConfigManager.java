package com.amongus.client.utils;

import com.amongus.client.AmongusClient;
import com.amongus.client.modules.Module;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.*;

public class ConfigManager {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File configDir = new File(mc.mcDataDir, "amongus/configs");
    private static final File defaultConfig = new File(configDir, "default.json");

    public static void saveConfig() {
        try {
            if (!configDir.exists()) configDir.mkdirs();
            Map<String, Object> data = new HashMap<>();
            List<Map<String, Object>> modulesData = new ArrayList<>();
            for (Module m : AmongusClient.moduleManager.getModules()) {
                Map<String, Object> modData = new HashMap<>();
                modData.put("name", m.getName());
                modData.put("enabled", m.isEnabled());
                Map<String, Object> settingsData = new HashMap<>();
                for (Module.Setting s : m.getSettings()) {
                    settingsData.put(s.getName(), s.getValue());
                }
                modData.put("settings", settingsData);
                modulesData.add(modData);
            }
            data.put("modules", modulesData);
            FileWriter writer = new FileWriter(defaultConfig);
            gson.toJson(data, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() {
        if (!defaultConfig.exists()) return;
        try {
            FileReader reader = new FileReader(defaultConfig);
            Map<String, Object> data = gson.fromJson(reader, Map.class);
            reader.close();
            List<Map<String, Object>> modulesData = (List<Map<String, Object>>) data.get("modules");
            if (modulesData == null) return;
            for (Map<String, Object> modData : modulesData) {
                String name = (String) modData.get("name");
                boolean enabled = (boolean) modData.get("enabled");
                Map<String, Object> settingsData = (Map<String, Object>) modData.get("settings");
                Module module = null;
                for (Module m : AmongusClient.moduleManager.getModules()) {
                    if (m.getName().equals(name)) {
                        module = m;
                        break;
                    }
                }
                if (module == null) continue;
                if (enabled && !module.isEnabled()) module.toggle();
                else if (!enabled && module.isEnabled()) module.toggle();
                if (settingsData != null) {
                    for (Map.Entry<String, Object> entry : settingsData.entrySet()) {
                        Module.Setting setting = module.getSetting(entry.getKey());
                        if (setting != null) {
                            setting.setValue(String.valueOf(entry.getValue()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
