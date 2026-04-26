//Mainmenu gui
package gui;

import gestures.GestureMapper;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import system.AppStateManager;
import system.CVProcessManager;
import system.GestureService;

public class MainMenu {

    private AppStateManager stateMgr;
    private CVProcessManager CVmgr;
    private GestureMapper gestMpr;
    private GestureService gestSrvc;
    private Thread gestThrd;
    private Timeline modeTmr;

    private Label Status;
    private Label ModeLabel;
    private Button toggleBtn;

    public MainMenu(AppStateManager stateMgr, CVProcessManager CVmgr, GestureMapper gestMpr) {
        this.stateMgr = stateMgr;
        this.CVmgr = CVmgr;
        this.gestMpr = gestMpr;
    }

    public void show(Stage stage) {
    //Headings
        Label title = new Label("ISHARA");
        title.setStyle(
            "-fx-font-size: 34px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: linear-gradient(to left, #00ffcc, #4facfe);"
        );

        Status = new Label("Status: IDLE");
        Status.setStyle("-fx-text-fill: #ffcc00;");

        ModeLabel = new Label("Mode: VOLUME");
        ModeLabel.setStyle("-fx-text-fill: #4facfe;");

        toggleBtn = new Button("Start");
        Button settingsBtn = new Button("Settings");
    //buttons
        toggleBtn.setPrefWidth(240);
        settingsBtn.setPrefWidth(240);
        toggleBtn.setPrefHeight(40);
        settingsBtn.setPrefHeight(40);

        String buttonStyle =
            "-fx-background-color: #2f3136;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;";

        String hoverStyle =
            "-fx-background-color: #40444b;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;";

        toggleBtn.setStyle(buttonStyle);
        settingsBtn.setStyle(buttonStyle);

        toggleBtn.setOnMouseEntered(e -> toggleBtn.setStyle(hoverStyle));
        toggleBtn.setOnMouseExited(e -> {
            // while cam running stay green
            if (stateMgr.isRunning()) {
                toggleBtn.setStyle("-fx-background-color: #3ba55d; -fx-text-fill: white; -fx-background-radius: 20;");
            } else {
                toggleBtn.setStyle(buttonStyle);
            }
        });

        settingsBtn.setOnMouseEntered(e -> settingsBtn.setStyle(hoverStyle));
        settingsBtn.setOnMouseExited(e -> settingsBtn.setStyle(buttonStyle));

        // Toggle button for Start & Stop
        toggleBtn.setOnAction(e -> {
            if (!stateMgr.isRunning()){
                onRun();
            }else{
                onStop();
            }
        });
//settings button
        settingsBtn.setOnAction(e -> {
            SettingsWindow settingsWindow = new SettingsWindow(gestMpr);
            settingsWindow.show(new Stage());
        });

        VBox layout = new VBox(15, title, Status, ModeLabel, toggleBtn, settingsBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1f22;");

        Scene scene = new Scene(layout, 450, 300);
        stage.setTitle("ISHARA");
        stage.setScene(scene);

        // Kill everything when window is closed
        stage.setOnCloseRequest(e -> {onStop();System.exit(0);});
        stage.show();
    }

    private void onRun() {
        CVmgr.start();
        try { Thread.sleep(3000);}catch (InterruptedException ignored){}
//giving python time to start^^
        gestSrvc = new GestureService(gestMpr);
        gestThrd = new Thread(gestSrvc);//create seperate thread
        gestThrd.setDaemon(true);        //for gesture service to avoid
        gestThrd.start();                    //ishara menu from freezing

        // check mode every 500ms to change menu mode name
        modeTmr = new Timeline(//timeline sets up an action the we repaet periodically
            new KeyFrame(Duration.millis(500), e -> {
                if (gestMpr.isMoveMode()) {
                    ModeLabel.setText("Mode: MOVE");
                    ModeLabel.setStyle("-fx-text-fill: orange;");
                } else {
                    ModeLabel.setText("Mode: VOLUME");
                    ModeLabel.setStyle("-fx-text-fill: #4facfe;");
                }
            })
        );
        modeTmr.setCycleCount(Timeline.INDEFINITE);
        modeTmr.play();

        stateMgr.setRunning();
        Status.setText("Status: RUNNING");
        Status.setStyle("-fx-text-fill: #3ba55d;");
        toggleBtn.setText("Stop");
        toggleBtn.setStyle("-fx-background-color: #3ba55d; -fx-text-fill: white; -fx-background-radius: 20;");
        System.out.println("[MainMenu] System started.");
    }

    private void onStop() {
        if(!stateMgr.isRunning()){return;}
        if(gestSrvc != null){gestSrvc.stop();}
        if(modeTmr != null){modeTmr.stop();}
        CVmgr.stop();
        stateMgr.setIdle();
        Status.setText("Status: IDLE");
        Status.setStyle("-fx-text-fill: #ffcc00;");
        ModeLabel.setText("Mode: VOLUME");
        ModeLabel.setStyle("-fx-text-fill: #4facfe;");
        toggleBtn.setText("Start");
        toggleBtn.setStyle(
            "-fx-background-color: #2f3136;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 20;" +
            "-fx-border-radius: 20;"
        );
        System.out.println("[MainMenu] System stopped.");
    }
}