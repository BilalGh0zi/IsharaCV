package system;

public class CVProcessManager {

    private static String PYTHON_SCRIPT = "C:\\Users\\Bilal\\Desktop\\IsharaApp\\Python\\gesture_server.py";

    private Process pythonProcess;
    public void start() {
        try{
            ProcessBuilder pb = new ProcessBuilder("python", PYTHON_SCRIPT);

            pb.redirectErrorStream(true);
            pb.inheritIO();

            pythonProcess = pb.start();
            System.out.println("[CVProcessManager] Python started.");   
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        } 
    }

    public void stop() {
        if (pythonProcess != null && pythonProcess.isAlive()) {
            pythonProcess.destroy();
            System.out.println("[CVProcessManager] Python stopped.");
        }
    }

    public boolean isRunning() {
        return pythonProcess != null && pythonProcess.isAlive();
    }
}