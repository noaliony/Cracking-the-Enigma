package tabs.screen;

import decryption.manager.DMInformationFromUser;
import engine.Engine;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import machine.details.ConfigurationDetails;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import machine.details.MachineDetailsObject;
import machine.details.MachineSetting;
import machineHistory.MachineHistory;
import tabs.bruteforce.BruteForceController;
import tabs.encryptdecrypt.EncryptDecryptController;
import tabs.machine.MachineController;
import tabs.machine.current.configuration.ConfigurationController;
import java.util.List;
import java.util.function.Consumer;

public class TabScreenController {

    @FXML private TabPane tabPane;
    @FXML private Tab encryptDecryptTab;
    @FXML private Tab bruteForceTab;
    @FXML private MachineController machineController;
    @FXML private EncryptDecryptController encryptDecryptController;
    @FXML private BruteForceController bruteForceController;
    @FXML private ConfigurationController currentConfigurationController;

    private BooleanProperty isRandomCodeSelected;

    public TabScreenController(){
        isRandomCodeSelected = new SimpleBooleanProperty();
    }

    @FXML
    private void initialize(){

        machineController.isRandomCodeSelectedProperty().addListener((observableValue, oldValue, newValue) -> {
            isRandomCodeSelected.set(newValue);
        });
        currentConfigurationController = new ConfigurationController();
        currentConfigurationController.setUp();
        setCurrentConfigurationController();
    }

    private void setCurrentConfigurationController() {
        machineController.setCurrentConfigurationController(currentConfigurationController);
        encryptDecryptController.setCurrentConfigurationController(currentConfigurationController);
        bruteForceController.setCurrentConfigurationController(currentConfigurationController);
    }

    public BooleanProperty isRandomCodeSelectedProperty() {
        return isRandomCodeSelected;
    }

    public void updateMachineDetails(MachineDetailsObject machineDetails) {
        machineController.updateMachineDetails(machineDetails);
    }

    public void setUpResetConfigurationButton(EventHandler<ActionEvent> resetConfigurationEvent) {
        encryptDecryptController.setUpResetConfigurationButton(resetConfigurationEvent);
        bruteForceController.setUpResetConfigurationButton(resetConfigurationEvent);
    }

    public void updateCurrentMachineConfiguration(MachineSetting machineSetting) {
        currentConfigurationController.updateCurrentMachineConfiguration(machineSetting);
    }

    public void setUpConfiguration(int rotorsCount, int rotorsCountOptionally, int reflectorsCount,
                                   List<Character> alphabet, Consumer<ConfigurationDetails> configurationDetailsConsumer,
                                   Consumer<String> errorMessageConsumer) {
        machineController.setUpConfiguration(rotorsCount, rotorsCountOptionally, reflectorsCount, alphabet,
                configurationDetailsConsumer, errorMessageConsumer);
    }

    public void setUpFullTextModeController(Consumer<String> fullTextModeProcessEvent) {
        encryptDecryptController.setUpFullTextModeController(fullTextModeProcessEvent);
    }
    public void setUpKeyByKeyModeController(Consumer<Character> keyByKeyModeProcessEvent) {
        encryptDecryptController.setUpKeyByKeyModeController(keyByKeyModeProcessEvent);
    }

    public String getTextIsEncodedMessageLabel() {
        return encryptDecryptController.getTextIsEncodedMessageLabel();
    }

    public void setTextProcessedMessage(String processedMessage) {
        encryptDecryptController.setTextProcessedMessage(processedMessage);
    }

    public void cleanTextFieldAndMessageLabel() {
        encryptDecryptController.cleanTextFieldAndMessageLabel();
    }

    public void setKeyByKeyModeDone(EventHandler<ActionEvent> actionEvent) {
        encryptDecryptController.setKeyByKeyModeDone(actionEvent);
    }

    public void addConfigurationHistoryViewToMachineHistory(MachineHistory currentMachineHistory) {
        encryptDecryptController.addConfigurationHistoryViewToMachineHistory(currentMachineHistory);
    }

    public void updateOriginalMachineConfiguration(MachineSetting machineSetting) {
        machineController.updateOriginalMachineConfiguration(machineSetting);
    }

    public void resetConfigurationsAndHistory() {
        machineController.resetOriginalConfiguration();
        machineController.clearAllConfigurations();
        currentConfigurationController.resetAllLabels();
        encryptDecryptController.resetHistory();
    }

    public void updateUserAboutMessagesAndTasksCount(DMInformationFromUser informationFromUser) {
        bruteForceController.updateUserAboutMessagesAndTasksCount(informationFromUser);
    }

    public void setUpBruteForce(Engine engine, Consumer<String> handleErrorConsumer,
                                Consumer<DMInformationFromUser> informationConsumer) {
        bruteForceController.setUp(engine, handleErrorConsumer,
                informationConsumer);
    }

    public void setUpTabScreen(BooleanProperty isConfigurationExist) {
        encryptDecryptTab.disableProperty().bind(isConfigurationExist.not());
        bruteForceTab.disableProperty().bind(isConfigurationExist.not());
        tabPane.getSelectionModel().select(0); // Select Machine Tab
    }
}
