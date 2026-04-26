package gestures;
import controllers.MouseController;

public class MoveRightGesture extends Gesture implements GestureAction {
    public MoveRightGesture(){ super("MOVE_RIGHT");}

    @Override
    public void execute(){ MouseController.moveRight();}
}