//manages settings and saving to json
package config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import system.PathFinder;
import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import system.PathFinder;
public class SettingsManager {

    private static SettingsManager instance;
    private static final Gson gson = new Gson(); //gson reads json

    private int spd= 10; //some default values
    private int volCldn= 100;
    private int clkCldn= 800;
    private int muteCldn= 1500;
    private int swtchCldn= 1500;
    private int mvCldn= 80;
    private int reqFrames = 4;

    
    private SettingsManager(){}
    public static SettingsManager getInstance(){ //prevents more than 1 settingsmanager
        if (instance == null){
            instance = new SettingsManager();
        }
        return instance;
    }

    // Gets settings path at runtime with PathFinder
    private String getSettingsPath() {
        return PathFinder.getInstance().getConfigPath("settings.json");
    }

    public void save(Map<String, String> gestureMap) {
        Map<String, String> SetMap = new HashMap<>(gestureMap);
        SetMap.put("__mouseSpeed",     String.valueOf(spd));
        SetMap.put("__volumeCooldown", String.valueOf(volCldn));
        SetMap.put("__clickCooldown",  String.valueOf(clkCldn));
        SetMap.put("__muteCooldown",   String.valueOf(muteCldn));
        SetMap.put("__switchCooldown", String.valueOf(swtchCldn));
        SetMap.put("__moveCooldown",   String.valueOf(mvCldn));
        SetMap.put("__framesRequired", String.valueOf(reqFrames));

        try (Writer writer = new FileWriter(getSettingsPath())) {
            gson.toJson(SetMap, writer);
            System.out.println("[SettingsManager] Settings saved.");
        } catch (IOException e) {
            System.out.println("[SettingsManager] Failed to save: " + e.getMessage());
        }
    }

    public Map<String, String> load(){
        try (Reader reader = new FileReader(getSettingsPath())) {
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> SetMap = gson.fromJson(reader, type);
            //the "__" prefix keeps json simple
            if (SetMap.containsKey("__mouseSpeed")){spd     = Integer.parseInt(SetMap.get("__mouseSpeed"));}
            if (SetMap.containsKey("__volumeCooldown")){volCldn = Integer.parseInt(SetMap.get("__volumeCooldown"));}
            if (SetMap.containsKey("__clickCooldown")){clkCldn  = Integer.parseInt(SetMap.get("__clickCooldown"));}
            if (SetMap.containsKey("__muteCooldown")){muteCldn   = Integer.parseInt(SetMap.get("__muteCooldown"));}
            if (SetMap.containsKey("__switchCooldown")){swtchCldn = Integer.parseInt(SetMap.get("__switchCooldown"));}
            if (SetMap.containsKey("__moveCooldown")){mvCldn   = Integer.parseInt(SetMap.get("__moveCooldown"));}
            if (SetMap.containsKey("__framesRequired")){reqFrames = Integer.parseInt(SetMap.get("__framesRequired"));}
            //only  keys prefixed by underscores are numerical values
            Map<String, String> gestureMap = new HashMap<>();
            for (Map.Entry<String, String> entry : SetMap.entrySet()) {
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
    //default map
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
//getters and setters
    public int getMouseSpeed(){ return spd;}
    public int getVolumeCooldown(){ return volCldn; }
    public int getClickCooldown(){ return clkCldn;}
    public int getMuteCooldown(){ return muteCldn;}
    public int getSwitchCooldown(){ return swtchCldn; }
    public int getMoveCooldown(){ return mvCldn;}
    public int getFramesRequired(){ return reqFrames; }

    public void setMouseSpeed(int v){ spd = v;}
    public void setVolumeCooldown(int v){ volCldn = v; }
    public void setClickCooldown(int v){ clkCldn = v;}
    public void setMuteCooldown(int v){ muteCldn = v;}
    public void setSwitchCooldown(int v){ swtchCldn = v; }
    public void setMoveCooldown(int v){ mvCldn = v; }
    public void setFramesRequired(int v){ reqFrames = v; }
}