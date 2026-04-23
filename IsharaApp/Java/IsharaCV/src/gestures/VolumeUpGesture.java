package gestures;

import controllers.VolumeController;

public class VolumeUpGesture extends Gesture implements GestureAction {

    public VolumeUpGesture() {
        super("VOLUME_UP");
    }

    @Override
    public void execute() {
        VolumeController.volumeUp();
    }
}