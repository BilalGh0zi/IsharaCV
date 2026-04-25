package gui;

import config.SettingsManager;
import gestures.GestureMapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SettingsWindow {

    private GestureMapper gestureMapper;

    public SettingsWindow(GestureMapper gestureMapper) {
        this.gestureMapper = gestureMapper;
    }

    public void show(Stage stage) {

       
        Label instructTitle = new Label("How to Use Ishara");
        instructTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label volumeHeader = new Label("VOLUME MODE (default)");
        volumeHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;");

        Label volumeInstructions = new Label(
            "ONE FINGER     →  Volume Up\n" +
            "TWO FINGERS    →  Volume Down\n" +
            "THREE FINGERS  →  Mute\n" +
            "FIST           →  Left Click\n" +
            "PINCH          →  Double Click\n" +
            "ROCK HAND      →  Switch to Move Mode"
        );
        volumeInstructions.setStyle("-fx-font-family: monospace; -fx-font-size: 13px;");

        Label moveHeader = new Label("MOVE MODE");
        moveHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: orange;");

        Label moveInstructions = new Label(
            "ONE FINGER     →  Move Up\n" +
            "TWO FINGERS    →  Move Right\n" +
            "THREE FINGERS  →  Move Left\n" +
            "FIST           →  Left Click\n" +
            "PINCH          →  Move Down\n" +
            "ROCK HAND      →  Switch to Volume Mode"
        );
        moveInstructions.setStyle("-fx-font-family: monospace; -fx-font-size: 13px;");

        VBox instructLayout = new VBox(12, instructTitle,
                                       volumeHeader, volumeInstructions,
                                       moveHeader, moveInstructions);
        instructLayout.setPadding(new Insets(20));

        Tab instructTab = new Tab("Instructions", instructLayout);
        instructTab.setClosable(false);

       

        SettingsManager s = SettingsManager.getInstance();

        Label tuningTitle = new Label("Performance Tuning");
        tuningTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        
        Slider mouseSpeedSlider    = makeSlider(1,  50, s.getMouseSpeed());
        Slider volumeCooldownSlider = makeSlider(50, 500, s.getVolumeCooldown());
        Slider clickCooldownSlider  = makeSlider(200, 2000, s.getClickCooldown());
        Slider muteCooldownSlider   = makeSlider(500, 3000, s.getMuteCooldown());
        Slider moveCooldownSlider   = makeSlider(20, 300, s.getMoveCooldown());
        Slider framesSlider         = makeSlider(1, 10, s.getFramesRequired());

        
        Label mouseSpeedVal    = makeValueLabel(mouseSpeedSlider);
        Label volumeCooldownVal = makeValueLabel(volumeCooldownSlider);
        Label clickCooldownVal  = makeValueLabel(clickCooldownSlider);
        Label muteCooldownVal   = makeValueLabel(muteCooldownSlider);
        Label moveCooldownVal   = makeValueLabel(moveCooldownSlider);
        Label framesVal         = makeValueLabel(framesSlider);

        GridPane tuningGrid = new GridPane();
        tuningGrid.setHgap(10);
        tuningGrid.setVgap(12);
        tuningGrid.setPadding(new Insets(15));

        addTuningRow(tuningGrid, 0, "Mouse Speed (px)",       mouseSpeedSlider,     mouseSpeedVal);
        addTuningRow(tuningGrid, 1, "Volume Cooldown (ms)",   volumeCooldownSlider, volumeCooldownVal);
        addTuningRow(tuningGrid, 2, "Click Cooldown (ms)",    clickCooldownSlider,  clickCooldownVal);
        addTuningRow(tuningGrid, 3, "Mute Cooldown (ms)",     muteCooldownSlider,   muteCooldownVal);
        addTuningRow(tuningGrid, 4, "Move Cooldown (ms)",     moveCooldownSlider,   moveCooldownVal);
        addTuningRow(tuningGrid, 5, "Gesture Sensitivity",    framesSlider,         framesVal);

       
        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(150);
        saveButton.setOnAction(e -> {
            s.setMouseSpeed((int) mouseSpeedSlider.getValue());
            s.setVolumeCooldown((int) volumeCooldownSlider.getValue());
            s.setClickCooldown((int) clickCooldownSlider.getValue());
            s.setMuteCooldown((int) muteCooldownSlider.getValue());
            s.setMoveCooldown((int) moveCooldownSlider.getValue());
            s.setFramesRequired((int) framesSlider.getValue());
            s.save(gestureMapper.getMappings());
            System.out.println("[SettingsWindow] Tuning saved.");
            stage.close();
        });

        VBox tuningLayout = new VBox(15, tuningTitle, tuningGrid, saveButton);
        tuningLayout.setAlignment(Pos.CENTER);
        tuningLayout.setPadding(new Insets(20));

        Tab tuningTab = new Tab("Tuning", tuningLayout);
        tuningTab.setClosable(false);

        
        TabPane tabPane = new TabPane(instructTab, tuningTab);

        stage.setTitle("Ishara Settings");
        stage.setScene(new Scene(tabPane, 420, 420));
        stage.show();
    }

   
    private Slider makeSlider(int min, int max, int value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit((max - min) / 4.0);
        slider.setPrefWidth(200);
        return slider;
    }

    
    private Label makeValueLabel(Slider slider) {
        Label label = new Label(String.valueOf((int) slider.getValue()));
        label.setMinWidth(40);
        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            label.setText(String.valueOf(newVal.intValue()));
        });
        return label;
    }

    
    private void addTuningRow(GridPane grid, int row, String labelText, Slider slider, Label valueLabel) {
        grid.add(new Label(labelText), 0, row);
        grid.add(slider, 1, row);
        grid.add(valueLabel, 2, row);
    }
}