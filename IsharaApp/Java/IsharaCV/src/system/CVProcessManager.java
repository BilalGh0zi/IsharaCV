//starts and stops the Python GestureServer 
package system;
import system.PathFinder;
public class CVProcessManager {
    private Process pyProcess;

    public void start(){
        try {
            //getting python path w the help of pathfinder
            String ServPath = PathFinder.getInstance().getPythonPath("gesture_server.py");
            String pyDir  = PathFinder.getInstance().getPythonPath("");

            
            //builds command to run python
            ProcessBuilder bob = new ProcessBuilder("python", ServPath, pyDir);
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
            pyProcess.destroy();
            System.out.println("[CVProcessManager] Python stopped.");
        }
    }

    public boolean isRunning(){return pyProcess != null && pyProcess.isAlive();}
}