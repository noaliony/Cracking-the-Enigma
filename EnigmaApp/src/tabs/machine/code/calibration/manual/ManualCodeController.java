package tabs.machine.code.calibration.manual;

import machine.details.ConfigurationDetails;
import enums.ReflectorID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import tabs.machine.code.calibration.manual.plug.board.PlugBoardController;
import tabs.machine.code.calibration.manual.reflector.id.ReflectorIDController;
import tabs.machine.code.calibration.manual.rotor.id.RotorIDController;
import tabs.machine.code.calibration.manual.start.position.StartPositionController;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ManualCodeController {

    @FXML private Button rotorsIDButton;
    @FXML private Button startPositionButton;
    @FXML private Button reflectorIDButton;
    @FXML private Button plugBoardButton;
    @FXML private Button setConfigurationButton;
    @FXML private Button clearConfigurationButton;
    @FXML private ScrollPane rotorID;
    @FXML private RotorIDController rotorIDController;
    @FXML private ScrollPane startPosition;
    @FXML private StartPositionController startPositionController;
    @FXML private ScrollPane reflectorID;
    @FXML private ReflectorIDController reflectorIDController;
    @FXML private ScrollPane plugBoard;
    @FXML private PlugBoardController plugBoardController;
    @FXML private StackPane manualConfigurationPanel;

    @FXML
    void rotorsIDButtonAction(ActionEvent event) {
        turnOffVisibleToManualConfigurationPanel();
        rotorID.setVisible(true);
    }

    @FXML
    void startPositionButtonAction(ActionEvent event) {
        turnOffVisibleToManualConfigurationPanel();
        startPosition.setVisible(true);
    }

    @FXML
    void reflectorIDButtonAction(ActionEvent event) {
        turnOffVisibleToManualConfigurationPanel();
        reflectorID.setVisible(true);
    }

    @FXML
    void plugBoardButtonAction(ActionEvent event) {
        turnOffVisibleToManualConfigurationPanel();
        plugBoard.setVisible(true);
    }

    private void turnOffVisibleToManualConfigurationPanel(){
        manualConfigurationPanel.getChildren().forEach(child -> child.setVisible(false));
    }

    private ConfigurationDetails getConfigurationDetails() {
        List<Integer> rotorsID = createRotorIDsString();
        List<Character> startPosition = createStartPositionString();
        ReflectorID reflectorID = createReflectorIDString();
        Map<Character, Character> plugPairs = createPlugPairsString();

        return new ConfigurationDetails(rotorsID, startPosition, reflectorID, plugPairs);
    }

    private List<Integer> createRotorIDsString() {
        return rotorIDController.createRotorIDsString();
    }

    private List<Character> createStartPositionString() {
        return startPositionController.createStartPositionString();
    }

    private ReflectorID createReflectorIDString() {
        return reflectorIDController.createReflectorIDString();
    }

    private Map<Character, Character> createPlugPairsString() {
        return plugBoardController.createPlugPairsString();
    }

    public void setUpConfiguration(int rotorsCount, int rotorsCountOptionally, int reflectorsCount,
                                   List<Character> alphabet, Consumer<ConfigurationDetails> configurationDetailsConsumer,
                                   Consumer<String> errorMessageConsumer) {
        rotorsIDButtonAction(null);
        rotorIDController.setUp(rotorsCount, rotorsCountOptionally);
        startPositionController.setUp(rotorsCount, alphabet);
        reflectorIDController.setUp(reflectorsCount);
        plugBoardController.setUp(alphabet);
        setConfigurationButton.setOnAction(event -> {
            try {
                configurationDetailsConsumer.accept(getConfigurationDetails());
            } catch (Exception exception) {
                errorMessageConsumer.accept(exception.getMessage());
            }
        });
        clearConfigurationButton.setOnAction(event -> clearAllConfigurations());
    }

    public void clearAllConfigurations() {
        rotorIDController.clearConfiguration();
        startPositionController.clearConfiguration();
        reflectorIDController.clearConfiguration();
        plugBoardController.clearConfiguration();
    }
}
