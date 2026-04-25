//uses JNA to controll particularly volume functions
package controllers;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.BYTE;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.win32.StdCallLibrary;

public class VolumeController {
//user32.dll is a Windows system file that handles keyboard/mouse input
    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = Native.load("user32", User32.class);

        void keybd_event(BYTE bVk, BYTE bScan, DWORD dwFlags, ULONG_PTR dwExtraInfo);
    }

    // Simulates pressing the Volume Up key (key code 0xAF)
    public static void volumeUp() {
        User32.INSTANCE.keybd_event(new BYTE((byte) 0xAF), new BYTE((byte) 0),new DWORD(0), new ULONG_PTR(0)); // key down
        User32.INSTANCE.keybd_event(new BYTE((byte) 0xAF), new BYTE((byte) 0),new DWORD(2), new ULONG_PTR(0)); // key up
        System.out.println("[VolumeController] Volume Up");
    }
    // Simulates pressing the Volume Down key (key code 0xAE)
    public static void volumeDown() {
        User32.INSTANCE.keybd_event(new BYTE((byte) 0xAE), new BYTE((byte) 0),new DWORD(0), new ULONG_PTR(0)); // key down
        User32.INSTANCE.keybd_event(new BYTE((byte) 0xAE), new BYTE((byte) 0),new DWORD(2), new ULONG_PTR(0)); // key up
        System.out.println("[VolumeController] Volume Down");
    }
    // Simulates pressing the Mute key (key code 0xAD)
    public static void mute() {
        User32.INSTANCE.keybd_event(new BYTE((byte) 0xAD), new BYTE((byte) 0),new DWORD(0), new ULONG_PTR(0)); // key down
        User32.INSTANCE.keybd_event(new BYTE((byte) 0xAD), new BYTE((byte) 0),new DWORD(2), new ULONG_PTR(0)); // key up
        System.out.println("[VolumeController] Mute toggled");
    }
}