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

    // Tuning values stored in memory
    private int mouseSpeed      = 10;
    private int volumeCooldown  = 100;
    private int clickCooldown   = 800;
    private int muteCooldown    = 1500;
    private int switchCooldown  = 1500;
    private int moveCooldown    = 80;
    private int framesRequired  = 4;

    private SettingsManager() {}

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    // -------------------------------------------------------
    // SAVE - writes both gesture map and tuning to one JSON
    // -------------------------------------------------------
    public void save(Map<String, String> gestureMap) {
        // Bundle everything into one map to save as JSON
        Map<String, String> fullMap = new HashMap<>(gestureMap);
        fullMap.put("__mouseSpeed",     String.valueOf(mouseSpeed));
        fullMap.put("__volumeCooldown", String.valueOf(volumeCooldown));
        fullMap.put("__clickCooldown",  String.valueOf(clickCooldown));
        fullMap.put("__muteCooldown",   String.valueOf(muteCooldown));
        fullMap.put("__switchCooldown", String.valueOf(switchCooldown));
        fullMap.put("__moveCooldown",   String.valueOf(moveCooldown));
        fullMap.put("__framesRequired", String.valueOf(framesRequired));

        try (Writer writer = new FileWriter(SETTINGS_PATH)) {
            gson.toJson(fullMap, writer);
            System.out.println("[SettingsManager] Settings saved.");
        } catch (IOException e) {
            System.out.println("[SettingsManager] Failed to save: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // LOAD - reads JSON and separates gesture map from tuning
    // -------------------------------------------------------
    public Map<String, String> load() {
        try (Reader reader = new FileReader(SETTINGS_PATH)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> fullMap = gson.fromJson(reader, type);

            // Extract tuning values (keys starting with __)
            if (fullMap.containsKey("__mouseSpeed"))     mouseSpeed     = Integer.parseInt(fullMap.get("__mouseSpeed"));
            if (fullMap.containsKey("__volumeCooldown")) volumeCooldown = Integer.parseInt(fullMap.get("__volumeCooldown"));
            if (fullMap.containsKey("__clickCooldown"))  clickCooldown  = Integer.parseInt(fullMap.get("__clickCooldown"));
            if (fullMap.containsKey("__muteCooldown"))   muteCooldown   = Integer.parseInt(fullMap.get("__muteCooldown"));
            if (fullMap.containsKey("__switchCooldown")) switchCooldown = Integer.parseInt(fullMap.get("__switchCooldown"));
            if (fullMap.containsKey("__moveCooldown"))   moveCooldown   = Integer.parseInt(fullMap.get("__moveCooldown"));
            if (fullMap.containsKey("__framesRequired")) framesRequired = Integer.parseInt(fullMap.get("__framesRequired"));

            // Return only gesture mappings (remove __ keys)
            Map<String, String> gestureMap = new HashMap<>();
            for (Map.Entry<String, String> entry : fullMap.entrySet()) {
                if (!entry.getKey().startsWith("__")) {
                    gestureMap.put(entry.getKey(), entry.getValue());
                }
            }

            System.out.println("[SettingsManager] Settings loaded.");
            return gestureMap;

        } catch (IOException e) {
            System.out.println("[SettingsManager] No settings file, using defaults.");
            return getDefaults();
        }
    }

    // Gesture map defaults
    private Map<String, String> getDefaults() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put("ONE_FINGER",   "VOLUME_UP");
        defaults.put("TWO_FINGER",   "VOLUME_DOWN");
        defaults.put("THREE_FINGER", "MUTE");
        defaults.put("FIST",         "LEFT_CLICK");
        defaults.put("PINCH",        "DOUBLE_CLICK");
        defaults.put("ROCK_HAND",    "SWITCH_MODE");
        return defaults;
    }

    // -------------------------------------------------------
    // GETTERS and SETTERS for tuning values
    // -------------------------------------------------------
    public int getMouseSpeed()      { return mouseSpeed; }
    public int getVolumeCooldown()  { return volumeCooldown; }
    public int getClickCooldown()   { return clickCooldown; }
    public int getMuteCooldown()    { return muteCooldown; }
    public int getSwitchCooldown()  { return switchCooldown; }
    public int getMoveCooldown()    { return moveCooldown; }
    public int getFramesRequired()  { return framesRequired; }

    public void setMouseSpeed(int v)      { mouseSpeed = v; }
    public void setVolumeCooldown(int v)  { volumeCooldown = v; }
    public void setClickCooldown(int v)   { clickCooldown = v; }
    public void setMuteCooldown(int v)    { muteCooldown = v; }
    public void setSwitchCooldown(int v)  { switchCooldown = v; }
    public void setMoveCooldown(int v)    { moveCooldown = v; }
    public void setFramesRequired(int v)  { framesRequired = v; }
}