package tabs.machine;

import decryption.manager.DMInformationFromUser;
import machine.details.ConfigurationDetails;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import machine.details.MachineDetailsObject;
import machine.details.MachineSetting;
import tabs.machine.code.calibration.CodeCalibrationController;
import tabs.machine.current.configuration.ConfigurationController;
import tabs.machine.details.view.MachineDetailsViewController;

import java.util.List;
import java.util.function.Consumer;

public class MachineController {
    @FXML private MachineDetailsViewController machineDetailsViewController;
    @FXML private CodeCalibrationController codeCalibrationController;
    @FXML private ConfigurationController currentConfigurationController;

    private BooleanProperty isRandomCodeSelected;

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

    public void updateMachineDetails(MachineDetailsObject machineDetails) {
        machineDetailsViewController.updateMachineDetails(machineDetails);
    }

    public void setUpConfiguration(int rotorsCount, int rotorsCountOptionally, int reflectorsCount,
                                   List<Character> alphabet, Consumer<ConfigurationDetails> configurationDetailsConsumer,
                                   Consumer<String> errorMessageConsumer) {
        codeCalibrationController.setUpConfiguration(rotorsCount, rotorsCountOptionally, reflectorsCount, alphabet,
                configurationDetailsConsumer, errorMessageConsumer);
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
}
