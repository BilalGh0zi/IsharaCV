//uses javas built in robot to move and click mouse
package controllers;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import config.SettingsManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class MouseController {

    private static Robot robot;

    static {
        try { robot = new Robot(); }
        catch (Exception e) { System.out.println("[MouseController] Robot's not waking up: " + e.getMessage()); }
    }

    public static void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        System.out.println("[MouseController] Left click");
    }

    public static void doubleClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(50);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        System.out.println("[MouseController] Double click");
    }

    // Reads pointer speed from SettingsManager 
    public static void moveUp() {
        int speed = SettingsManager.getInstance().getMouseSpeed();
        Point p = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(p.x, p.y - speed);
    }

    public static void moveDown() {
        int speed = SettingsManager.getInstance().getMouseSpeed();
        Point p = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(p.x, p.y + speed);
    }

    public static void moveLeft() {
        int speed = SettingsManager.getInstance().getMouseSpeed();
        Point p = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(p.x - speed, p.y);
    }

    public static void moveRight() {
        int speed = SettingsManager.getInstance().getMouseSpeed();
        Point p = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(p.x + speed, p.y);
    }
}