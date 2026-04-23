package system;

public class AppStateManager {

    private boolean CamRunning = false;

    public boolean isRunning() {
        return CamRunning;
    }

    public void setRunning() {
        CamRunning = true;
        System.out.println("[AppStateManager] State -> RUNNING");
    }

    public void setIdle() {
        CamRunning = false;
        System.out.println("[AppStateManager] State -> IDLE");
    }
}