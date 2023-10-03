package tabs.machine.current.configuration;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import machine.details.MachineSetting;

public class ConfigurationController {

    @FXML private Label rotorsAndNotchesValueLabel;
    @FXML private Label startingPositionValueLabel;
    @FXML private Label reflectorIDValueLabel;
    @FXML private Label plugBoardValueLabel;
    @FXML private Label allCodeConfigurationValueLabel;

    public void setUp(){
        rotorsAndNotchesValueLabel = new Label();
        startingPositionValueLabel = new Label();
        reflectorIDValueLabel = new Label();
        plugBoardValueLabel = new Label();
        allCodeConfigurationValueLabel = new Label();
    }

    public void bindToConfigurationController(ConfigurationController controllerToBind) {
        rotorsAndNotchesValueLabel.textProperty().bind(controllerToBind.rotorsAndNotchesValueLabel.textProperty());
        startingPositionValueLabel.textProperty().bind(controllerToBind.startingPositionValueLabel.textProperty());
        reflectorIDValueLabel.textProperty().bind(controllerToBind.reflectorIDValueLabel.textProperty());
        plugBoardValueLabel.textProperty().bind(controllerToBind.plugBoardValueLabel.textProperty());
        allCodeConfigurationValueLabel.textProperty().bind(controllerToBind.allCodeConfigurationValueLabel.textProperty());
    }

    public void setTextRotorsAndNotchesValueLabel(String rotorsAndNotchesValueLabel) {
        this.rotorsAndNotchesValueLabel.setText(rotorsAndNotchesValueLabel);
    }

    public void setTextStartingPositionValueLabel(String startingPositionValueLabel) {
        this.startingPositionValueLabel.setText(startingPositionValueLabel);
    }

    public void setTextReflectorIDValueLabel(String reflectorIDValueLabel) {
        this.reflectorIDValueLabel.setText(reflectorIDValueLabel);
    }

    public void setTextPlugBoardValueLabel(String plugBoardValueLabel) {
        this.plugBoardValueLabel.setText(plugBoardValueLabel);
    }

    public void setTextAllCodeConfigurationValueLabel(String allCodeConfigurationValueLabel) {
        this.allCodeConfigurationValueLabel.setText(allCodeConfigurationValueLabel);
    }

    public void updateCurrentMachineConfiguration(MachineSetting machineSetting) {
        setTextRotorsAndNotchesValueLabel(machineSetting.createStringRotors().toString());
        setTextStartingPositionValueLabel(machineSetting.createStartingPosition().toString());
        setTextReflectorIDValueLabel(machineSetting.createReflectorID().toString());
        setTextPlugBoardValueLabel(machineSetting.createPlugPairs().toString());
        setTextAllCodeConfigurationValueLabel(machineSetting.toString());
    }

    public void resetAllLabels() {
        rotorsAndNotchesValueLabel.setText("");
        startingPositionValueLabel.setText("");
        reflectorIDValueLabel.setText("");
        plugBoardValueLabel.setText("");
        allCodeConfigurationValueLabel.setText("");
    }
}

