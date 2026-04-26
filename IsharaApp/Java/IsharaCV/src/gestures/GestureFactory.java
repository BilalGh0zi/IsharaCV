//creates a new gesture object depending on the input with switch
package gestures;

public class GestureFactory {

    public static Gesture create(String Name) {

        switch (Name) {
//volume factory
            case "VOLUME_UP":
                return new VolumeUpGesture();

            case "VOLUME_DOWN":
                return new VolumeDownGesture();

            case "MUTE":
                return new MuteGesture();
//Clickfactory                
            case "LEFT_CLICK":
                return new LeftClickGesture();

            case "DOUBLE_CLICK":
                return new DoubleClickGesture();
//movement factory            
            case "MOVE_UP":
                return new MoveUpGesture();

            case "MOVE_DOWN":
                return new MoveDownGesture();

            case "MOVE_LEFT":
                return new MoveLeftGesture();

            case "MOVE_RIGHT":
                return new MoveRightGesture();
            default:
                System.out.println("[GestureFactory] Unknown action: " + Name);
                return null;
        }
    }
}