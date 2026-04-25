package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsWindow {

    public void show(Stage stage) {

        Label title = new Label("Settings");
        title.setStyle(
                "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(to left, #00ffcc, #4facfe);"
        );

        Button mouseMode = new Button("Mouse Movement Mode");
        Button volumeMode = new Button("Volume Control Mode");

        styleButton(mouseMode);
        styleButton(volumeMode);

        mouseMode.setPrefWidth(220);
        volumeMode.setPrefWidth(220);

        mouseMode.setPrefHeight(34);
        volumeMode.setPrefHeight(34);

        Label pointerLabel = new Label("Pointer Speed");
        pointerLabel.setStyle("-fx-text-fill: white;");
        Slider pointerSpeed = new Slider(0, 100, 50);

        Label volumeLabel = new Label("Volume Change Speed");
        volumeLabel.setStyle("-fx-text-fill: white;");
        Slider volumeSpeed = new Slider(0, 100, 50);

        Label gestureLabel = new Label("Gesture Sensitivity");
        gestureLabel.setStyle("-fx-text-fill: white;");
        Slider gestureSensitivity = new Slider(0, 100, 50);

        makeGradientSlider(pointerSpeed);
        makeGradientSlider(volumeSpeed);
        makeGradientSlider(gestureSensitivity);

        pointerSpeed.valueProperty().addListener((obs, o, n) ->
                pointerLabel.setText("Pointer Speed: " + n.intValue() + "%")
        );

        volumeSpeed.valueProperty().addListener((obs, o, n) ->
                volumeLabel.setText("Volume Speed: " + n.intValue() + "%")
        );

        gestureSensitivity.valueProperty().addListener((obs, o, n) ->
                gestureLabel.setText("Gesture Sensitivity: " + n.intValue() + "%")
        );

        mouseMode.setOnAction(e ->
                System.out.println("Mouse Movement Mode selected")
        );

        volumeMode.setOnAction(e ->
                System.out.println("Volume Control Mode selected")
        );

        Button saveBtn = new Button("Save");
        styleButton(saveBtn);
        saveBtn.setPrefWidth(220);
        saveBtn.setPrefHeight(34);

        saveBtn.setOnAction(e -> {
            int p = (int) pointerSpeed.getValue();
            int v = (int) volumeSpeed.getValue();
            int g = (int) gestureSensitivity.getValue();

            System.out.println("=== SETTINGS SAVED ===");
            System.out.println("Pointer Speed: " + p + "%");
            System.out.println("Volume Speed: " + v + "%");
            System.out.println("Gesture Sensitivity: " + g + "%");
        });

        VBox settingsLayout = new VBox(10,
                title,
                mouseMode,
                volumeMode,
                pointerLabel,
                pointerSpeed,
                volumeLabel,
                volumeSpeed,
                gestureLabel,
                gestureSensitivity,
                saveBtn
        );

        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.setPadding(new Insets(15));
        settingsLayout.setStyle("-fx-background-color: #1e1f22;");

        // ================= INSTRUCTIONS (HARDCODED, NON-EDITABLE) =================
        Label instructionsLabel = new Label(
                "How to Use Ishara\n\n" +

                        "VOLUME MODE (default)\n\n" +
                        "ONE FINGER      → Volume Up\n" +
                        "TWO FINGERS     → Volume Down\n" +
                        "THREE FINGERS   → Mute\n" +
                        "FIST            → Left Click\n" +
                        "PINCH           → Double Click\n" +
                        "ROCK HAND       → Switch to Move Mode\n\n" +

                        "MOVE MODE\n\n" +
                        "ONE FINGER      → Move Up\n" +
                        "TWO FINGERS     → Move Right\n" +
                        "THREE FINGERS   → Move Left\n" +
                        "FIST            → Left Click\n" +
                        "PINCH           → Move Down\n" +
                        "ROCK HAND       → Switch to Volume Mode"
        );

        instructionsLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-family: 'Consolas';"
        );

        instructionsLabel.setWrapText(true);

        VBox instructionsLayout = new VBox(instructionsLabel);
        instructionsLayout.setPadding(new Insets(15));
        instructionsLayout.setStyle("-fx-background-color: #1e1f22;");

        TabPane tabPane = new TabPane();

        Tab settingsTab = new Tab("Settings", settingsLayout);
        Tab instructionsTab = new Tab("Instructions", instructionsLayout);

        settingsTab.setClosable(false);
        instructionsTab.setClosable(false);

        tabPane.getTabs().addAll(settingsTab, instructionsTab);

        Scene scene = new Scene(tabPane, 450, 420);

        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }

    private void styleButton(Button btn) {

        String baseStyle =
                "-fx-background-color: #2f3136;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;";

        String hoverStyle =
                "-fx-background-color: #40444b;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;";

        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
    }

    private void makeGradientSlider(Slider slider) {

        slider.setStyle(
                "-fx-control-inner-background: #2f3136;" +
                        "-fx-background-color: transparent;"
        );

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {

            double percent = newVal.doubleValue() / slider.getMax();

            String gradient =
                    "linear-gradient(to right, #9b59b6 " +
                            (percent * 100) + "%, #2f3136 " +
                            (percent * 100) + "%)";

            slider.applyCss();
            slider.lookup(".track").setStyle(
                    "-fx-background-color: " + gradient + ";"
            );
        });
    }
}