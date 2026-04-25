import gui.SettingsWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuTest extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("ISHARA");
        title.setStyle(
                "-fx-font-size: 34px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(to left, #00ffcc, #4facfe);"
        );

        Label status = new Label("Status: IDLE");
        status.setStyle("-fx-text-fill: #ffcc00;");

        Button toggleBtn = new Button("Start");
        Button settingsBtn = new Button("Settings");

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
        toggleBtn.setOnMouseExited(e -> toggleBtn.setStyle(buttonStyle));

        settingsBtn.setOnMouseEntered(e -> settingsBtn.setStyle(hoverStyle));
        settingsBtn.setOnMouseExited(e -> settingsBtn.setStyle(buttonStyle));

        boolean[] isRunning = {false};

        toggleBtn.setOnAction(e -> {
            isRunning[0] = !isRunning[0];

            if (isRunning[0]) {

                toggleBtn.setText("Stop");
                status.setText("Status: RUNNING");
                status.setStyle("-fx-text-fill: #3ba55d;");

                System.out.println("[SYSTEM] ISHARA ENGINE STARTED");
                toggleBtn.setStyle("-fx-background-color: #3ba55d; -fx-text-fill: white; -fx-background-radius: 20;");

            } else {

                toggleBtn.setText("Start");
                status.setText("Status: IDLE");
                status.setStyle("-fx-text-fill: #ffcc00;");

                System.out.println("[SYSTEM] ISHARA ENGINE STOPPED");
                toggleBtn.setStyle(buttonStyle);
            }
        });

        settingsBtn.setOnAction(e -> {
            SettingsWindow settingsWindow = new SettingsWindow();
            Stage settingsStage = new Stage();
            settingsWindow.show(settingsStage);
        });

        VBox layout = new VBox(15, title, status, toggleBtn, settingsBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1f22;");

        Scene scene = new Scene(layout, 450, 300);

        stage.setTitle("Main Menu Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}