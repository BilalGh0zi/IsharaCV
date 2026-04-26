//ts file listens for inputs from the python server and forwards them to gesturemapper
package system;
//imports
import gestures.GestureMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class GestureService implements Runnable{
    //portnumber for tcp socket
    private static final int PORT = 6767;//funni number
    private GestureMapper mapist;
    private boolean isrunning = false;
    private Socket Sock;

    public GestureService(GestureMapper mapist){this.mapist = mapist;}

public void run(){
        isrunning = true;
        try {
            int maxAttempts = 20; 
            for (int i = 0; i < maxAttempts; i++) {
                try {
                    Sock = new Socket("localhost", PORT);
                    System.out.println("[GestureService] Lesgooooo Connected to Python!"); 
                    break;
                    
                } 
                catch (Exception e) {
                    System.out.println("[GestureService] Waiting for server... (" + (i+1) + "/" + maxAttempts + ")");
                    Thread.sleep(500);
                }
            }
            if (Sock == null) {
                System.out.println("[GestureService] Connection error: Could not reach Python server.");
                return;
            }
//wrap input from python and read 1 line at a time vv
            BufferedReader reader = new BufferedReader(new InputStreamReader(Sock.getInputStream()));

            String ts;
            while (isrunning && (ts = reader.readLine()) != null) {
                String Name = ts.trim();//removes spaces etc.
                mapist.handle(Name);// method from gesturmapper
                
                //System.out.println(Name);
            }

        } 
         catch (Exception e){
            if (isrunning) {
            System.out.println("[GestureService] Connection error: " + e.getMessage());
            }
        }
    }





public void stop(){//to close da socket when stopped
    isrunning = false;
    try {if (Sock != null){Sock.close();}}
    catch(Exception e) {
        System.out.println("[GestureService] Error stopping" + e.getMessage());
    }
    System.out.println("[GestureService] Stopped.");

}
}