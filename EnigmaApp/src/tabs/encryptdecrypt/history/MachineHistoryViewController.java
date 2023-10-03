package tabs.encryptdecrypt.history;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import machineHistory.MachineHistory;
import machineHistory.ProcessedString;
import tabs.encryptdecrypt.history.configuration.history.view.ConfigurationHistoryView;

public class MachineHistoryViewController {

    @FXML private HBox historyTableHBox;

    private ConfigurationHistoryView currentConfigurationHistoryView;

    private void addConfiguration(MachineHistory machineHistory){
        ConfigurationHistoryView configurationHistoryView = new ConfigurationHistoryView(machineHistory.getMachineSetting().toString());
        Separator separator = new Separator();

        separator.setOrientation(Orientation.VERTICAL);
        historyTableHBox.getChildren().addAll(configurationHistoryView, separator);
        currentConfigurationHistoryView = configurationHistoryView;
    }

    private void addProcessedString(ProcessedString processedString){
        currentConfigurationHistoryView.addProcessedString(processedString);
    }


    public void addConfigurationHistoryViewToMachineHistory(MachineHistory machineHistory) {
        if (currentConfigurationHistoryView == null ||
                !currentConfigurationHistoryView.getTextConfigurationTitleLabel().equals(machineHistory.getMachineSetting().toString())) {
            addConfiguration(machineHistory);
        }
        turnOffAllConfigurationHistoryView();
        addProcessedString(machineHistory.getCurrentProcessedString());
    }

    private void turnOffAllConfigurationHistoryView() {
        historyTableHBox.getChildren().forEach(child -> {

            if (child instanceof ConfigurationHistoryView && child != currentConfigurationHistoryView){
                ((ConfigurationHistoryView) child).turnOffLessOrMoreViewButton();
            }
        });
    }

    public void resetHistory() {
        historyTableHBox.getChildren().clear();
    }
}
