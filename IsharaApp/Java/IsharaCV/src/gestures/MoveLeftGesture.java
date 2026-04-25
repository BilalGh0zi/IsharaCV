package gestures;
import controllers.MouseController;

public class MoveLeftGesture extends Gesture implements GestureAction {
    public MoveLeftGesture() { super("MOVE_LEFT"); }

    @Override
    public void execute() { MouseController.moveLeft(); }
}