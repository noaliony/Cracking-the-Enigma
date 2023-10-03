package main.uboat.tabs.screen.machine.code.calibration.manual.rotor.id;

import exceptions.RotorIDsAreNotValidException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class RotorIDController {
    @FXML private HBox rotorsIDHBox;
    @FXML private Button clearConfigurationButton;

    private int rotorsCount;
    private int rotorsCountOptionally;

    public void setUp(int rotorsCount, int rotorsCountOptionally){
        this.rotorsCount = rotorsCount;
        this.rotorsCountOptionally = rotorsCountOptionally;

        rotorsIDHBox.getChildren().clear();
        for (int i = 1; i <= rotorsCount; i++) {
            rotorsIDHBox.getChildren().add(createComboBox());
        }
        clearConfigurationButton.setOnAction(event -> clearConfiguration());
    }

    private ComboBox<Integer> createComboBox() {
        ComboBox<Integer> optionalRotorsID = new ComboBox<>();
        ObservableList<Integer> observableList = optionalRotorsID.getItems();

        optionalRotorsID.valueProperty().addListener((observable, oldValue, newValue) ->
                updateAllComboBoxes(oldValue, newValue, optionalRotorsID));

        for (int i = 1; i <= rotorsCountOptionally; i++) {
            observableList.add(i);
        }

        return optionalRotorsID;
    }

    private void updateAllComboBoxes(Integer oldValue, Integer newValue, ComboBox<Integer> optionalRotorsID) {
        rotorsIDHBox.getChildren().forEach(comboBox -> {
            if (optionalRotorsID != comboBox) {
                ComboBox<Integer> comboBoxCast = (ComboBox<Integer>) comboBox;

                if (oldValue == null && newValue != null) {
                    removeRotorIDFromComboBox(newValue, comboBoxCast);
                } else if (oldValue != null) {
                    addRotorIDToComboBox(oldValue, comboBoxCast);
                    if (newValue != null) {
                        removeRotorIDFromComboBox(newValue, comboBoxCast);
                    }
                }
            }
        });
    }

    private void addRotorIDToComboBox(Integer oldValue, ComboBox<Integer> comboBox) {
        comboBox.getItems().add(oldValue);
        FXCollections.sort(comboBox.getItems());
    }

    private void removeRotorIDFromComboBox(Integer newValue, ComboBox<Integer> comboBox) {
        comboBox.getItems().remove(newValue);
    }

    public List<Integer> createRotorIDsString() {
        List<Integer> result = new ArrayList<>();

        rotorsIDHBox.getChildren().forEach(comboBox -> {
            ComboBox<Integer> comboBoxCast = (ComboBox<Integer>)comboBox;

            if (comboBoxCast.getValue() == null)
                throw new RotorIDsAreNotValidException();
            result.add(comboBoxCast.getValue());
        });

        return result;
    }

    public void clearConfiguration() {
        for (int i = 0; i < 2; i++) {
            rotorsIDHBox.getChildren().forEach(comboBox -> {
                ComboBox<Integer> comboBoxCast = (ComboBox<Integer>) comboBox;

                comboBoxCast.getItems().clear();
            });
        }

        rotorsIDHBox.getChildren().forEach(comboBox -> {
            ComboBox<Integer> comboBoxCast = (ComboBox<Integer>) comboBox;

            for (int i = 1; i <= rotorsCountOptionally; i++) {
                comboBoxCast.getItems().add(i);
            }
        });
    }
}
