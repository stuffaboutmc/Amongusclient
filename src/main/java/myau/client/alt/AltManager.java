package myau.client.alt;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.util.*;

public class AltManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static List<AltEntry> alts = new ArrayList<>();
    private static File file;

    public static void init() {
        file = new File(System.getProperty("user.home"), ".amongusclient/alts.json");
        file.getParentFile().mkdirs();
        load();
    }

    public static void add(String name, String pass, boolean cracked) {
        alts.add(new AltEntry(name, pass, cracked));
        save();
    }

    public static void remove(int index) {
        if (index >= 0 && index < alts.size()) { alts.remove(index); save(); }
    }

    public static List<AltEntry> getAlts() { return alts; }

    public static AltEntry getRandom() {
        if (alts.isEmpty()) return null;
        return alts.get(new Random().nextInt(alts.size()));
    }

    private static void save() {
        try (FileWriter fw = new FileWriter(file)) { GSON.toJson(alts, fw); } catch (Exception e) {}
    }

    private static void load() {
        if (!file.exists()) return;
        try { alts = GSON.fromJson(new FileReader(file), new TypeToken<List<AltEntry>>(){}.getType()); if (alts == null) alts = new ArrayList<>(); } catch (Exception e) { alts = new ArrayList<>(); }
    }

    public static class AltEntry {
        public String name, password;
        public boolean cracked;
        public AltEntry(String name, String password, boolean cracked) { this.name = name; this.password = password; this.cracked = cracked; }
    }
}
