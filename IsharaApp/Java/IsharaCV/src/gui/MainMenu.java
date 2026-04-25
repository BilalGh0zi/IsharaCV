package gui;

import gestures.GestureMapper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import system.AppStateManager;
import system.CVProcessManager;
import system.GestureService;

public class MainMenu {

    private AppStateManager stateManager;
    private CVProcessManager cvManager;
    private GestureMapper gestureMapper;
    private GestureService gestureService;
    private Thread gestureThread;

    private Label RunStatus;
    private Label ModeLabel;

    public MainMenu(AppStateManager stateManager,CVProcessManager cvManager,GestureMapper gestureMapper) {
        this.stateManager = stateManager;
        this.cvManager = cvManager;
        this.gestureMapper = gestureMapper;
    }

    public void show(Stage stage) {

        Label Title = new Label("ISHARA");
        Title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        RunStatus = new Label("Status: IDLE");
        RunStatus.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

       
        ModeLabel = new Label("Mode: VOLUME");
        ModeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");

        Button runButton = new Button("Run");
        runButton.setPrefWidth(200);
        runButton.setStyle("-fx-font-size: 14px;");
        runButton.setOnAction(e -> onRun());

        Button stopButton = new Button("Stop");
        stopButton.setPrefWidth(200);
        stopButton.setStyle("-fx-font-size: 14px;");
        stopButton.setOnAction(e -> onStop());

        Button settingsButton = new Button("Settings");
        settingsButton.setPrefWidth(200);
        settingsButton.setStyle("-fx-font-size: 14px;");
        settingsButton.setOnAction(e -> {
            SettingsWindow settingsWindow = new SettingsWindow(gestureMapper);
            settingsWindow.show(new Stage());
        });

        VBox layout = new VBox(15, Title, RunStatus, ModeLabel,
                               runButton, stopButton, settingsButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));

        Scene scene = new Scene(layout, 350, 350);
        stage.setTitle("IsharaApp");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            onStop();
            System.exit(0);
        });

        stage.show();
    }

    private void onRun() {
        if (stateManager.isRunning()) {
            System.out.println("[MainMenu] Already running.");
            return;
        }

        cvManager.start();

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        gestureService = new GestureService(gestureMapper);
        gestureThread = new Thread(gestureService);
        gestureThread.setDaemon(true);
        gestureThread.start();

        //fix modelabel bilal
        
                if (gestureMapper.isMoveMode()) {
                    ModeLabel.setText("Mode: MOVE");
                    ModeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: orange;");
                } else {
                    ModeLabel.setText("Mode: VOLUME");
                    ModeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");
                }
            ;
        ;

        stateManager.setRunning();
        RunStatus.setText("Status: RUNNING");
        RunStatus.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");
        System.out.println("[MainMenu] System started.");
    }

    private void onStop() {
        if (!stateManager.isRunning()) return;
        if (gestureService != null) gestureService.stop();

        cvManager.stop();

       
        ModeLabel.setText("Mode: VOLUME");
        ModeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");

        stateManager.setIdle();
        RunStatus.setText("Status: IDLE");
        RunStatus.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        System.out.println("[MainMenu] System stopped.");
    }
}