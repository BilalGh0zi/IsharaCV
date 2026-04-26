//starts and stops the Python GestureServer 
package system;
import java.io.IOException;

import system.PathFinder;
public class CVProcessManager {
    private Process pyProcess;

    public void start(){
        try {

            // Clear the port 
            KilPort(6767);

            //getting python path w the help of pathfinder
            String ServPath = PathFinder.getInstance().getPythonPath("gesture_server.exe");
            String pyDir  = PathFinder.getInstance().getPythonPath("");

            
            //builds command to run python
            ProcessBuilder bob = new ProcessBuilder( ServPath, pyDir);
            bob.redirectErrorStream(true);
            bob.inheritIO();// links IO stream to java

            pyProcess = bob.start();//Fires off the python process
            System.out.println("[CVProcessManager] Python started.");

        } 
        catch (Exception e){
            System.out.println("[CVProcessManager] Python isnt waking up : " + e.getMessage());
        }
    }

    public void stop(){
        if (pyProcess != null && pyProcess.isAlive()) {
            KilPort(6767);
            System.out.println("[CVProcessManager] Python stopped.");
        }
    }
    private void KilPort(int port) {
    try {
        String command = "cmd /c \"for /f \"tokens=5\" %a in ('netstat -aon ^| findstr :" + port + "') do taskkill /f /pid %a\"";
        Runtime.getRuntime().exec(command);
        System.out.println("[CVProcessManager] Cleared port " + port);
        
        Thread.sleep(500); 
    } 
    catch (Exception e) {
        System.out.println("[CVProcessManager] Port was already clear.");
    }
}
    public boolean isRunning(){return pyProcess != null && pyProcess.isAlive();}
}