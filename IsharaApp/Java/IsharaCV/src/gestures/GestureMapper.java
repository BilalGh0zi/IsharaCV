package gestures;

import config.SettingsManager;
import java.util.Map;
import java.util.HashMap;

public class GestureMapper {

    private Map<String, String> gestureMap;
    private Map<String, Long> lastExecutedTime = new HashMap<>();

    
    private static final long COOLDOWN_VOLUME = 100;
    private static final long COOLDOWN_MUTE    = 1500;
    private static final long COOLDOWN_CLICK   = 800;

    public GestureMapper() {
        gestureMap = SettingsManager.getInstance().load();
    }

    private long getCooldown(String gestureName) {
        switch (gestureName) {
            case "ONE_FINGER":   return COOLDOWN_VOLUME;
            case "TWO_FINGERS":  return COOLDOWN_VOLUME;
            case "THREE_FINGERS":return COOLDOWN_MUTE;
            case "FIST":         return COOLDOWN_CLICK;
            case "PINCH":        return COOLDOWN_CLICK;
            default:             return 1000;
        }
    }

    public void handle(String gestureName) {

        if (gestureName == null || gestureName.equals("NONE")) return;

        String action = gestureMap.get(gestureName);
        if (action == null) {
            System.out.println("[GestureMapper] No mapping for: " + gestureName);
            return;
        }

        long now = System.currentTimeMillis();
        long lastTime = lastExecutedTime.getOrDefault(gestureName, 0L);
        if (now - lastTime < getCooldown(gestureName)) return;

        lastExecutedTime.put(gestureName, now);

        Gesture gesture = GestureFactory.create(action);
        if (gesture != null) {
            gesture.execute();
        }
    }

    public void updateMapping(String gestureName, String action) {
        gestureMap.put(gestureName, action);
        SettingsManager.getInstance().save(gestureMap);
    }

    public Map<String, String> getMappings() {
        return gestureMap;
    }
}