package main.uboat.tabs.screen.machine.code.calibration.manual.reflector.id;

import enums.GameLevel;
import enums.ReflectorID;
import exceptions.ReflectorIDNotSelectedException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class ReflectorIDController {
    @FXML private ComboBox<String> reflectorIDComboBox;
    @FXML private Button clearConfigurationButton;

    private int reflectorsCount;

    public void setUp(int reflectorsCount){
        this.reflectorsCount = reflectorsCount;

        reflectorIDComboBox.getItems().clear();
        for (int i = 0; i < reflectorsCount; i++) {
            reflectorIDComboBox.getItems().add(ReflectorID.values()[i].name());
        }
        clearConfigurationButton.setOnAction(event -> clearConfiguration());
    }

    public ReflectorID createReflectorIDString() {
        if (reflectorIDComboBox.getValue() == null){
            throw new ReflectorIDNotSelectedException();
        }
        return ReflectorID.convertStringToReflectorID(reflectorIDComboBox.getValue());
    }

    public void clearConfiguration() {
        reflectorIDComboBox.setValue(null);
    }
}
