//Double click gesture
package gestures;
import controllers.MouseController;

public class DoubleClickGesture extends Gesture implements GestureAction {

    public DoubleClickGesture() {
        super("DOUBLE_CLICK");
    }

    @Override
    public void execute() {
        MouseController.doubleClick();
    }
}