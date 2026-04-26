package gestures;

import controllers.MouseController;
public class LeftClickGesture extends Gesture implements GestureAction {

    public LeftClickGesture() {
        super("LEFT_CLICK");
    }

    @Override
    public void execute(){
        MouseController.leftClick();
    }
}