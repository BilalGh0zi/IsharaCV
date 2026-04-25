package gestures;
import controllers.MouseController;

public class MoveDownGesture extends Gesture implements GestureAction {
    public MoveDownGesture() { super("MOVE_DOWN"); }

    @Override
    public void execute() { MouseController.moveDown(); }
}