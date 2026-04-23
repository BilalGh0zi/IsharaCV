package system;

import gestures.GestureMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class GestureService implements Runnable{

    private static final int PORT = 6767;

    private GestureMapper map;
    private boolean running = false;
    private Socket socket;

    public GestureService(GestureMapper map) {
        this.map = map;
    }

    public void run() {
        running = true;
        try {
            socket = new Socket("localhost", PORT);
            System.out.println("[GestureService] Connected to Python!");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while (running && (line = reader.readLine()) != null) {
                String gestureName = line.trim();
                map.handle(gestureName);
            }

        } catch (Exception e) {
            if (running) {
                System.out.println("[GestureService] Connection error: " + e.getMessage());
            }
        }
    }


    public void stop() {
        running = false;
        try {
            if (socket != null){ socket.close();}
        } catch (Exception e) {
            System.out.println("[GestureService] Error stopping: " + e.getMessage());
        }
        System.out.println("[GestureService] Stopped.");
    }
}