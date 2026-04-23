package gestures;

import controllers.VolumeController;

public class MuteGesture extends Gesture implements GestureAction{
  public MuteGesture() {
        
        super("MUTE");
    }

    @Override
    public void execute() {
        VolumeController.mute();
        
    }
}