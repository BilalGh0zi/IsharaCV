package controllers;

import java.awt.Robot;
import java.awt.event.InputEvent;
public class MouseController {

    private static Robot robot;

    
    static {
        try {robot = new Robot();} 
        catch (Exception e) {System.out.println("[MouseController] Robot's not waking up" + e.getMessage());}
    }

    
    public static void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        System.out.println("[MouseController] Left click");
    }

   
    public static void doubleClick() {
        leftClick();
        robot.delay(50); // small delay between clicks
        leftClick();
        System.out.println("[MouseController] Double click");
    }
}