package tabs.machine.code.calibration;

import machine.details.ConfigurationDetails;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import machine.details.MachineSetting;
import tabs.machine.code.calibration.manual.ManualCodeController;
import tabs.machine.code.calibration.random.RandomCodeController;
import tabs.machine.current.configuration.ConfigurationController;

import java.util.List;
import java.util.function.Consumer;

public class CodeCalibrationController {

    @FXML private ConfigurationController originalConfigurationController;
    @FXML private RandomCodeController randomCodeController;
    @FXML private ManualCodeController manualCodeController;

    private BooleanProperty isRandomCodeSelected;

    public CodeCalibrationController(){
        isRandomCodeSelected = new SimpleBooleanProperty();
    }

    @FXML
    private void initialize(){

        randomCodeController.isRandomCodeSelectedProperty().addListener((observableValue, oldValue, newValue) -> {
            isRandomCodeSelected.set(newValue);
        });
    }

    public BooleanProperty isRandomCodeSelectedProperty() {
        return isRandomCodeSelected;
    }

    public void setUpConfiguration(int rotorsCount, int rotorsCountOptionally, int reflectorsCount,
                                   List<Character> alphabet, Consumer<ConfigurationDetails> configurationDetailsConsumer,
                                   Consumer<String> errorMessageConsumer) {
        manualCodeController.setUpConfiguration(rotorsCount, rotorsCountOptionally, reflectorsCount, alphabet,
                configurationDetailsConsumer, errorMessageConsumer);
    }

    public void updateOriginalMachineConfiguration(MachineSetting machineSetting) {
        originalConfigurationController.updateCurrentMachineConfiguration(machineSetting);
    }

    public void resetOriginalConfiguration() {
        originalConfigurationController.resetAllLabels();
    }

    public void clearAllConfigurations() {
        manualCodeController.clearAllConfigurations();
    }
}