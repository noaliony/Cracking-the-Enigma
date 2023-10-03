package tabs.encryptdecrypt;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import machineHistory.MachineHistory;
import tabs.encryptdecrypt.history.MachineHistoryViewController;
import tabs.encryptdecrypt.input.processor.InputProcessorController;
import tabs.machine.current.configuration.ConfigurationController;
import java.util.function.Consumer;

public class EncryptDecryptController {

    @FXML private ConfigurationController currentConfigurationController;
    @FXML private InputProcessorController inputProcessorController;
    @FXML private MachineHistoryViewController machineHistoryViewController;
    @FXML private Button resetConfigurationButton;


    public void setUpFullTextModeController(Consumer<String> fullTextModeProcessEvent) {
        inputProcessorController.setUpFullTextModeController(fullTextModeProcessEvent);
    }

    public void setUpKeyByKeyModeController(Consumer<Character> keyByKeyModeProcessEvent) {
        inputProcessorController.setUpKeyByKeyModeController(keyByKeyModeProcessEvent);
    }

    public void setUpResetConfigurationButton(EventHandler<ActionEvent> resetConfigurationEvent) {
        resetConfigurationButton.setOnAction(resetConfigurationEvent);
    }

    public void setCurrentConfigurationController(ConfigurationController configurationController) {
        this.currentConfigurationController.bindToConfigurationController(configurationController);
    }

    public String getTextIsEncodedMessageLabel() {
        return inputProcessorController.getTextIsEncodedMessage();
    }

    public void setTextProcessedMessage(String processedMessage) {
        inputProcessorController.setTextIsEncodedMessage(processedMessage);
    }

    public void cleanTextFieldAndMessageLabel() {
        inputProcessorController.cleanTextFieldAndMessageLabel();
    }

    public void setKeyByKeyModeDone(EventHandler<ActionEvent> actionEvent) {
        inputProcessorController.setKeyByKeyModeDone(actionEvent);
    }

    public void addConfigurationHistoryViewToMachineHistory(MachineHistory currentMachineHistory) {
        machineHistoryViewController.addConfigurationHistoryViewToMachineHistory(currentMachineHistory);
    }

    public void resetHistory() {
        machineHistoryViewController.resetHistory();
    }
}
