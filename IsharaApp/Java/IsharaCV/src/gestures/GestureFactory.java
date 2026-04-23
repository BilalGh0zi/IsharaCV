package gestures;

public class GestureFactory {

    public static Gesture create(String actionName) {

        switch (actionName) {

            case "VOLUME_UP":
                return new VolumeUpGesture();

            case "VOLUME_DOWN":
                return new VolumeDownGesture();

            case "MUTE":
                return new MuteGesture();
                
            case "LEFT_CLICK":
                return new LeftClickGesture();

            case "DOUBLE_CLICK":
                return new DoubleClickGesture();

            default:
                System.out.println("[GestureFactory] Unknown action: " + actionName);
                return null;
        }
    }
}