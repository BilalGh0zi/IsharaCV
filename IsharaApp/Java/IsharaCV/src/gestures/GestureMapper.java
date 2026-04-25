package gestures;

import config.SettingsManager;
import java.util.Map;
import java.util.HashMap;

public class GestureMapper {

    private boolean moveMode = false;
    private Map<String, String> gestureMap;
    private Map<String, Long> lastExecutedTime = new HashMap<>();

    public interface ModeChangeListener {
        void onModeChanged(boolean isMoveMode);
    }

    private ModeChangeListener modeChangeListener;

    public GestureMapper() {
        gestureMap = SettingsManager.getInstance().load();
    }

    public void setModeChangeListener(ModeChangeListener listener) {
        this.modeChangeListener = listener;
    }

    public boolean isMoveMode() {
        return moveMode;
    }

    // Reads cooldowns live from SettingsManager so changes take effect immediately
    private long getCooldown(String gestureName) {
        SettingsManager s = SettingsManager.getInstance();
        switch (gestureName) {
            case "ONE_FINGER":   return moveMode ? s.getMoveCooldown() : s.getVolumeCooldown();
            case "TWO_FINGER":   return moveMode ? s.getMoveCooldown() : s.getVolumeCooldown();
            case "THREE_FINGER": return moveMode ? s.getMoveCooldown() : s.getMuteCooldown();
            case "FIST":         return s.getClickCooldown();
            case "PINCH":        return moveMode ? s.getMoveCooldown() : s.getClickCooldown();
            case "ROCK_HAND":    return s.getSwitchCooldown();
            default:             return 1000;
        }
    }

    private String resolveAction(String gestureName) {

        if (gestureName.equals("FIST"))      return "LEFT_CLICK";
        if (gestureName.equals("ROCK_HAND")) return "SWITCH_MODE";

        if (!moveMode) {
            switch (gestureName) {
                case "ONE_FINGER":   return "VOLUME_UP";
                case "TWO_FINGER":   return "VOLUME_DOWN";
                case "THREE_FINGER": return "MUTE";
                case "PINCH":        return "DOUBLE_CLICK";
                default:             return null;
            }
        } else {
            switch (gestureName) {
                case "ONE_FINGER":   return "MOVE_UP";
                case "TWO_FINGER":   return "MOVE_RIGHT";
                case "THREE_FINGER": return "MOVE_LEFT";
                case "PINCH":        return "MOVE_DOWN";
                default:             return null;
            }
        }
    }

    public void handle(String gestureName) {

        if (gestureName == null || gestureName.equals("NONE")) return;

        long now = System.currentTimeMillis();
        long lastTime = lastExecutedTime.getOrDefault(gestureName, 0L);
        if (now - lastTime < getCooldown(gestureName)) return;
        lastExecutedTime.put(gestureName, now);

        if (gestureName.equals("ROCK_HAND")) {
            moveMode = !moveMode;
            System.out.println("[GestureMapper] Switched to: " + (moveMode ? "MOVE" : "VOLUME"));
            if (modeChangeListener != null) {
                modeChangeListener.onModeChanged(moveMode);
            }
            return;
        }

        String action = resolveAction(gestureName);
        if (action == null) {
            System.out.println("[GestureMapper] No action for: " + gestureName);
            return;
        }

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