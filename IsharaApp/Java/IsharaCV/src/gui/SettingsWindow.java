package gui;

import gestures.GestureMapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SettingsWindow {

    private GestureMapper gestureMapper;
private static final String[] GESTURES = {"ONE_FINGER", "TWO_FINGERS", "THREE_FINGERS", "FIST", "PINCH"};
private static final String[] ACTIONS = {"VOLUME_UP", "VOLUME_DOWN", "MUTE", "LEFT_CLICK", "DOUBLE_CLICK", "NONE"};

    public SettingsWindow(GestureMapper gestureMapper) {
        this.gestureMapper = gestureMapper;
    }

    public void show(Stage stage) {

        Label titleLabel = new Label("Gesture Settings");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

  
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        
        grid.add(new Label("Gesture"), 0, 0);
        grid.add(new Label("Action"), 1, 0);

   
        ComboBox<String>[] combos = new ComboBox[GESTURES.length];
        for (int i = 0; i < GESTURES.length; i++) {
            String gesture = GESTURES[i];

            grid.add(new Label(gesture), 0, i + 1);

            ComboBox<String> combo = new ComboBox<>();
            combo.getItems().addAll(ACTIONS);

            String currentAction = gestureMapper.getMappings().getOrDefault(gesture, "NONE");
            combo.setValue(currentAction);

            combos[i] = combo;
            grid.add(combo, 1, i + 1);
        }

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(150);
        saveButton.setOnAction(e -> {
        
            for (int i = 0; i < GESTURES.length; i++) {
                gestureMapper.updateMapping(GESTURES[i], combos[i].getValue());
            }
            System.out.println("[SettingsWindow] Settings saved.");
            stage.close(); 
        });

        VBox layout = new VBox(15, titleLabel, grid, saveButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        stage.setTitle("Settings");
        stage.setScene(new Scene(layout, 350, 400));
        stage.show();
    }
}