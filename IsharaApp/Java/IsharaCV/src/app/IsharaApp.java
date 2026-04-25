//App entry point
package app;

import gestures.GestureMapper;
import gui.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;
import system.AppStateManager;
import system.CVProcessManager;

public class IsharaApp extends Application {

    @Override
    public void start(Stage primaryStage) {

 //creating objects for main managers
        AppStateManager stateManager=new AppStateManager();
        CVProcessManager cvManager=new CVProcessManager();
        GestureMapper gestureMapper=new GestureMapper(); 
        //creating ishara mainmenu
        MainMenu mainMenu=new MainMenu(stateManager, cvManager, gestureMapper);
        mainMenu.show(primaryStage);
    }

public static void main(String[] args) {
    launch(args);//launch ui and call start()
    }
}