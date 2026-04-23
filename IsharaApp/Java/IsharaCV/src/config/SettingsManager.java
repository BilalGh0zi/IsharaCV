package config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SettingsManager {

    private static SettingsManager instance;
    private static final String SETTINGS_PATH = "C:\\Users\\Bilal\\Desktop\\IsharaApp\\config\\settings.json";
    private static final Gson gson = new Gson();

    private SettingsManager() {}

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public void save(Map<String, String> gestureMap) {
        try (Writer writer = new FileWriter(SETTINGS_PATH)) {
            gson.toJson(gestureMap, writer);
            System.out.println("[SettingsManager] Settings updated.");
        } catch (IOException e) {
            System.out.println("[SettingsManager] Failed to save: " + e.getMessage());
        }
    }

    public Map<String, String> load() {
        try (Reader reader = new FileReader(SETTINGS_PATH)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> map = gson.fromJson(reader, type);
            System.out.println("[SettingsManager] Settings loaded.");
            return map;
        } catch (IOException e) {
            System.out.println("[SettingsManager] No settings file, using defaults.");
            return getDefaults();
        }
    }

    private Map<String, String> getDefaults() {
    Map<String, String> defaults = new HashMap<>();
    defaults.put("ONE_FINGER",    "VOLUME_UP");
    defaults.put("TWO_FINGERS",   "VOLUME_DOWN");
    defaults.put("THREE_FINGERS", "MUTE");
    defaults.put("FIST",          "LEFT_CLICK");
    defaults.put("PINCH",         "DOUBLE_CLICK");
    return defaults;
}
}