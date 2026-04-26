//Settings Window gui
package gui;

import config.SettingsManager;
import gestures.GestureMapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsWindow {

    private GestureMapper gestureMapper;

    public SettingsWindow(GestureMapper gestureMapper) {
        this.gestureMapper = gestureMapper;
    }

    //functions that help convert slider's percentage to values
    private double mapToPercent(int value, int min, int max) {
        if (max == min) return 0;
        return ((double)(value - min) / (max - min)) * 100.0;
    }
    private int mapFromPercent(double percent, int min, int max) {
        return (int) Math.round(min + (percent / 100.0) * (max - min));
    }
    public void show(Stage stage) {
        //makes sure only one instance of Settingsmanager exists ata time
        //see SettingsManager for context
        SettingsManager s = SettingsManager.getInstance();

        Label title = new Label("Settings");
        title.setStyle(
        "-fx-font-size: 30px;" +
        "-fx-font-weight: bold;" +
        "-fx-text-fill: linear-gradient(to left, #00ffcc, #4facfe);"
        );

        Button mouseMode  = new Button("Mouse Movement Mode");
        Button volumeMode = new Button("Volume Control Mode");
        styleButton(mouseMode);
        styleButton(volumeMode);
        mouseMode.setPrefWidth(220);
        volumeMode.setPrefWidth(220);
        mouseMode.setPrefHeight(34);
        volumeMode.setPrefHeight(34);


        
        
        // VOLUME MODE SLIDERS
        
        Slider volumeSpeedSlider = makeGradientSlider();//from 0 to 450 millisec
        volumeSpeedSlider.setValue(mapToPercent(500 - s.getVolumeCooldown(), 0, 450));
        Label volumeSpeedLabel = makeSliderLabel("Volume Change Speed", volumeSpeedSlider);

        Slider muteCooldownSlider = makeGradientSlider();
        muteCooldownSlider.setValue(mapToPercent(3000 - s.getMuteCooldown(), 0, 2500));
        Label muteCooldownLabel = makeSliderLabel("Mute Cooldown", muteCooldownSlider);

        Slider GestureSensSlider = makeGradientSlider();// Gesture sensitivity
        GestureSensSlider.setValue(mapToPercent(10 - s.getFramesRequired(), 0, 9));
        Label GestureSensLabel = makeSliderLabel("Gesture Sensitivity", GestureSensSlider);

        VBox volumeSliders = new VBox(8,
            volumeSpeedLabel, volumeSpeedSlider,
            muteCooldownLabel, muteCooldownSlider,
            GestureSensLabel, GestureSensSlider
        );

        
        
        // MOUSE MODE SLIDERS
        
        Slider mouseSpeedSlider = makeGradientSlider();// how many px mouse moves at a time
        mouseSpeedSlider.setValue(mapToPercent(s.getMouseSpeed(), 1, 50));
        Label mouseSpeedLabel = makeSliderLabel("Pointer Speed", mouseSpeedSlider);

        Slider moveCooldownSlider = makeGradientSlider();// how often mouse moves
        moveCooldownSlider.setValue(mapToPercent(300 - s.getMoveCooldown(), 0, 280));
        Label moveCooldownLabel = makeSliderLabel("Move Cooldown", moveCooldownSlider);

        Slider gestureSensSlider = makeGradientSlider();//sensitivityl
        gestureSensSlider.setValue(mapToPercent(10 - s.getFramesRequired(), 0, 9));
        Label gestureSensLabel = makeSliderLabel("Gesture Sensitivity", gestureSensSlider);

        VBox mouseSliders = new VBox(8,
            mouseSpeedLabel, mouseSpeedSlider,
            moveCooldownLabel, moveCooldownSlider,
            gestureSensLabel, gestureSensSlider
        );

        
        // Starts with volume sliders shown by default
        VBox activeSliders = new VBox(volumeSliders);

        // Mode button swaps which sliders are visible
        mouseMode.setOnAction(e -> {activeSliders.getChildren().setAll(mouseSliders);});
        volumeMode.setOnAction(e -> {activeSliders.getChildren().setAll(volumeSliders);});


        // SAVE BUTTON
        
        Button saveBtn = new Button("Save");
        styleButton(saveBtn);
        saveBtn.setPrefWidth(220);
        saveBtn.setPrefHeight(34);

        saveBtn.setOnAction(e -> {
            // Save volume settings
            s.setVolumeCooldown(500 - mapFromPercent(volumeSpeedSlider.getValue(), 0, 450));
            s.setMuteCooldown(3000 - mapFromPercent(muteCooldownSlider.getValue(), 0, 2500));

            // Save mouse settings
            s.setMouseSpeed(mapFromPercent(mouseSpeedSlider.getValue(), 1, 50));
            s.setMoveCooldown(300 - mapFromPercent(moveCooldownSlider.getValue(), 0, 280));

            // Gesture sensitivity 
            // both sliders map to same setting so just save one
            s.setFramesRequired(10 - mapFromPercent(GestureSensSlider.getValue(), 0, 9));

            s.save(gestureMapper.getMappings());
            System.out.println("[SettingsWindow] Settings saved.");
            stage.close();
        });

        // SETTINGS layout
        VBox settingsLayout = new VBox(10,
            title,
            mouseMode,
            volumeMode,
            activeSliders,
            saveBtn
        );
        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.setPadding(new Insets(15));
        settingsLayout.setStyle("-fx-background-color: #1e1f22;");

       
        // Instructions Section
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
    
        
        // Tabs
        Tab settingsTab     = new Tab("Settings",     settingsLayout);
        Tab instructionsTab = new Tab("Instructions", instructionsLayout);
        settingsTab.setClosable(false);
        instructionsTab.setClosable(false);

        TabPane tabPane = new TabPane(settingsTab, instructionsTab);

        stage.setTitle("Settings");
        stage.setScene(new Scene(tabPane, 450, 420));
        stage.show();
    }
    

    
    // Aesthetic helper functions
 
    private Label makeSliderLabel(String name, Slider slider) {
        Label label = new Label(name + ": " + (int) slider.getValue() + "%");
        label.setStyle("-fx-text-fill: white;");
        slider.valueProperty().addListener((obs, o, n) ->
            label.setText(name + ": " + n.intValue() + "%")
        );
        return label;
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

    private Slider makeGradientSlider() {
        Slider slider = new Slider(0, 100, 50);
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

        return slider;
    }
}