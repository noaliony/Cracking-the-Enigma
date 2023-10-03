package main.uboat.tabs.screen.machine;

import dto.ConfigurationDetails;
import dto.MachineSetting;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import main.uboat.tabs.screen.machine.code.calibration.CodeCalibrationController;
import main.uboat.tabs.screen.machine.current.configuration.ConfigurationController;
import main.uboat.tabs.screen.machine.details.view.MachineDetailsViewController;

import java.util.function.Consumer;

public class MachineController {
    @FXML private MachineDetailsViewController machineDetailsViewController;
    @FXML private CodeCalibrationController codeCalibrationController;
    @FXML private ConfigurationController currentConfigurationController;

    private BooleanProperty isRandomCodeSelected;
    private Consumer<String> handleErrorConsumer;

    public MachineController(){
        isRandomCodeSelected = new SimpleBooleanProperty();
    }

    @FXML
    private void initialize(){

        codeCalibrationController.isRandomCodeSelectedProperty().addListener((observableValue, oldValue, newValue) -> {
            isRandomCodeSelected.set(newValue);
        });
    }

    public void setCurrentConfigurationController(ConfigurationController configurationController) {
        this.currentConfigurationController.bindToConfigurationController(configurationController);
    }

    public BooleanProperty isRandomCodeSelectedProperty() {
        return isRandomCodeSelected;
    }

    public void setUp(Runnable setRandomCodeRunnable){
        codeCalibrationController.setUp(setRandomCodeRunnable);
    }

    public void sendRequestToGetMachineDetails() {
        machineDetailsViewController.sendRequestToGetMachineDetails();
        codeCalibrationController.sendRequestToGetMachineDetails();
    }

    public void setUpConfiguration(Consumer<ConfigurationDetails> configurationDetailsConsumer,
                                   Consumer<String> errorMessageConsumer,
                                   Runnable setConfigurationRunnable) {
        codeCalibrationController.setUpConfiguration(configurationDetailsConsumer, errorMessageConsumer,
                setConfigurationRunnable);
    }

    public void updateOriginalMachineConfiguration(MachineSetting machineSetting) {
        codeCalibrationController.updateOriginalMachineConfiguration(machineSetting);
    }

    public void resetOriginalConfiguration() {
        codeCalibrationController.resetOriginalConfiguration();
    }

    public void clearAllConfigurations() {
        codeCalibrationController.clearAllConfigurations();
    }

    public void sendRequestToSetRandomCode() {
        codeCalibrationController.sendRequestToSetRandomCode();
    }

    public void sendRequestToSetCodeConfiguration(ConfigurationDetails configurationDetails) {
        codeCalibrationController.sendRequestToSetCodeConfiguration(configurationDetails);
    }

    public void setUpHandleErrorConsumer(Consumer<String> handleErrorConsumer) {
        this.handleErrorConsumer = handleErrorConsumer;
        machineDetailsViewController.setUpHandleErrorConsumer(handleErrorConsumer);
        codeCalibrationController.setUpHandleErrorConsumer(handleErrorConsumer);
    }

    public void advanceAndSetTextAmountOfIsEncodedMessagesLabel() {
        machineDetailsViewController.advanceAndSetTextAmountOfIsEncodedMessagesLabelRunnable();
    }

    public void reset() {
        codeCalibrationController.reset();
    }
}
