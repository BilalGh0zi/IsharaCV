package gestures;
import controllers.MouseController;

public class MoveUpGesture extends Gesture implements GestureAction {
    public MoveUpGesture(){ super("MOVE_UP");}

    @Override
    public void execute(){ MouseController.moveUp();}
}