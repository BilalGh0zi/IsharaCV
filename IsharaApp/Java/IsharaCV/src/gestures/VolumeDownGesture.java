package gestures;

import controllers.VolumeController;

public class VolumeDownGesture extends Gesture implements GestureAction{
  public VolumeDownGesture() {
        super("VOLUME_DOWN");
    }

    @Override
    public void execute() {
        VolumeController.volumeDown();
    }
}