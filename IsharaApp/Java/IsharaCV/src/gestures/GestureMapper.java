//maps Actions to recieved Gestures and executes
package gestures;

import config.SettingsManager;
import java.util.Map;
import java.util.HashMap;

public class GestureMapper {

    private boolean moveMode = false;//bool to switch modes
    private Map<String, String> gestureMap;
    private Map<String, Long> CooldownTime = new HashMap<>();

    public GestureMapper() {
        gestureMap = SettingsManager.getInstance().load();
    }
    public boolean isMoveMode() {
        return moveMode;
    }

    // Reads cooldowns from SettingsManager 
    private long getCooldown(String name) {
        SettingsManager s = SettingsManager.getInstance();
        switch (name) {
            case "ONE_FINGER":   return moveMode ? s.getMoveCooldown() : s.getVolumeCooldown();
            case "TWO_FINGER":   return moveMode ? s.getMoveCooldown() : s.getVolumeCooldown();
            case "THREE_FINGER": return moveMode ? s.getMoveCooldown() : s.getMuteCooldown();
            case "FIST":         return s.getClickCooldown();//click in both modes
            case "PINCH":        return moveMode ? s.getMoveCooldown() : s.getClickCooldown();
            case "ROCK_HAND":    return s.getSwitchCooldown();
            default:             return 1000;
        }
    }

    private String ChooseAction(String name) {

    if (name.equals("FIST")){ return "LEFT_CLICK";}
    if (name.equals("ROCK_HAND")) {return "SWITCH_MODE";}

    if (moveMode) {
        switch (name) {
                case "ONE_FINGER":   return "MOVE_UP";
                case "TWO_FINGER":   return "MOVE_RIGHT";
                case "THREE_FINGER": return "MOVE_LEFT";
                case "PINCH":        return "MOVE_DOWN";
                default:             return null;
            }
        } 
    else {      switch (name) {
                case "ONE_FINGER":   return "VOLUME_UP";
                case "TWO_FINGER":   return "VOLUME_DOWN";
                case "THREE_FINGER": return "MUTE";
                case "PINCH":        return "DOUBLE_CLICK";
                default:             return null;
            }
        }
    }

    public void handle(String name) {

        if (name == null || name.equals("NONE")){ return;}

        long thisTime = System.currentTimeMillis();
        long prev = CooldownTime.getOrDefault(name, 0L);
        if (thisTime - prev < getCooldown(name)){ return;}//cooldown check
        CooldownTime.put(name, thisTime);

        //mode change logic
        if (name.equals("ROCK_HAND")) {
            if (moveMode){moveMode = false;}
            else if (!moveMode){moveMode = true;}
            System.out.println("[GestureMapper] Switched to: " + (moveMode ? "MOVE" : "VOLUME"));
            return;
        }
        //take corresponding action from name
        String action = ChooseAction(name);
        if (action == null) {
            System.out.println("[GestureMapper] No action for: " + name);
            return;
        }
            //execute gesture
        Gesture gesture = GestureFactory.create(action);
        if (gesture != null) {
            gesture.execute();
        }
    }

    public void updateMapping(String name, String action) {
        gestureMap.put(name, action);
        SettingsManager.getInstance().save(gestureMap);
    }

    public Map<String, String> getMappings() {
        return gestureMap;
    }
}