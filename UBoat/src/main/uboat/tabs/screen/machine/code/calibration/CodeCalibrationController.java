package main.uboat.tabs.screen.machine.code.calibration;

import dto.ConfigurationDetails;
import dto.MachineSetting;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import main.uboat.tabs.screen.machine.code.calibration.manual.ManualCodeController;
import main.uboat.tabs.screen.machine.code.calibration.random.RandomCodeController;
import main.uboat.tabs.screen.machine.current.configuration.ConfigurationController;

import java.util.function.Consumer;

public class CodeCalibrationController {

    @FXML private ConfigurationController originalConfigurationController;
    @FXML private RandomCodeController randomCodeController;
    @FXML private ManualCodeController manualCodeController;

    private BooleanProperty isRandomCodeSelected;
    private Consumer<String> handleErrorConsumer;

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

    public void setUp(Runnable setRandomCodeRunnable){
        randomCodeController.setUp(setRandomCodeRunnable);
    }

    public void setUpConfiguration(Consumer<ConfigurationDetails> configurationDetailsConsumer,
                                   Consumer<String> errorMessageConsumer,
                                   Runnable setConfigurationRunnable) {
        manualCodeController.setUpConfiguration(configurationDetailsConsumer, errorMessageConsumer,
                setConfigurationRunnable);
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

    public void sendRequestToSetRandomCode() {
        randomCodeController.sendRequestToSetRandomCode();
    }

    public void sendRequestToSetCodeConfiguration(ConfigurationDetails configurationDetails) {
        manualCodeController.sendRequestToSetCodeConfiguration(configurationDetails);
    }

    public void sendRequestToGetMachineDetails() {
        manualCodeController.sendRequestToGetMachineDetails();
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        randomCodeController.setUpHandleErrorConsumer(handleErrorConsumer);
        manualCodeController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void reset() {
        manualCodeController.clearAllConfigurations();
    }
}