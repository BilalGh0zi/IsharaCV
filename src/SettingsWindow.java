package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsWindow {

    public void show(Stage stage) {

        Label title = new Label("Settings");
        title.setStyle(
                "-fx-font-size: 34px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(to left, #00ffcc, #4facfe);"
        );

        Button mouseMode = new Button("Mouse Movement Mode");
        Button volumeMode = new Button("Volume Control Mode");

        styleButton(mouseMode);
        styleButton(volumeMode);

        mouseMode.setPrefWidth(240);
        volumeMode.setPrefWidth(240);

        mouseMode.setPrefHeight(40);
        volumeMode.setPrefHeight(40);

        Label pointerLabel = new Label("Pointer Speed");
        pointerLabel.setStyle("-fx-text-fill: white;");

        Slider pointerSpeed = new Slider(0, 100, 50);

        Label volumeLabel = new Label("Volume Change Speed");
        volumeLabel.setStyle("-fx-text-fill: white;");

        Slider volumeSpeed = new Slider(0, 100, 50);

        // ================= APPLY GRADIENT SLIDER EFFECT =================
        makeGradientSlider(pointerSpeed);
        makeGradientSlider(volumeSpeed);

        // ================= LIVE FEEDBACK =================
        pointerSpeed.valueProperty().addListener((obs, oldVal, newVal) -> {
            pointerLabel.setText("Pointer Speed: " + newVal.intValue() + "%");
            System.out.println("Pointer Speed: " + newVal.intValue() + "%");
        });

        volumeSpeed.valueProperty().addListener((obs, oldVal, newVal) -> {
            volumeLabel.setText("Volume Speed: " + newVal.intValue() + "%");
            System.out.println("Volume Speed: " + newVal.intValue() + "%");
        });

        mouseMode.setOnAction(e -> {
            System.out.println("Mouse Movement Mode selected");
        });

        volumeMode.setOnAction(e -> {
            System.out.println("Volume Control Mode selected");
        });

        VBox layout = new VBox(15,
                title,
                mouseMode,
                volumeMode,
                pointerLabel,
                pointerSpeed,
                volumeLabel,
                volumeSpeed
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #1e1f22;");

        Scene scene = new Scene(layout, 450, 300);

        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }

    // ================= BUTTON STYLE =================
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

    // ================= GRADIENT SLIDER LOGIC =================
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